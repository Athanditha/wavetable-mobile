package com.example.wavetable.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wavetable.ApiService
import com.example.wavetable.model.ApiResponse
import com.example.wavetable.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

class ItemViewModel(application: Application) : AndroidViewModel(application) {

    // StateFlow to store the list of items (for listing)
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    val items: StateFlow<List<Item>> = _items

    // StateFlow to store a single item (for detailed view)
    private val _item = MutableStateFlow<Item?>(null)
    val item: StateFlow<Item?> = _item

    private val _searchResults = MutableStateFlow<List<Item>>(emptyList())
    val searchResults: StateFlow<List<Item>> = _searchResults

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val api = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        )
        .client(OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                Log.d("ItemViewModel", "Making request to: ${request.url}")
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build())
        .build()
        .create(ApiService::class.java)

    private val gson = Gson()

    private val context = application.applicationContext
    private val itemsCacheFileName = "items_cache.json"
    private val itemDetailsCacheDir = "item_details"

    private fun saveItemsToCache(items: List<Item>) {
        try {
            val gson = Gson()
            val jsonString = gson.toJson(items)
            context.openFileOutput(itemsCacheFileName, Context.MODE_PRIVATE).use { stream ->
                stream.write(jsonString.toByteArray())
            }
            Log.d("ItemViewModel", "Successfully saved items to cache")
        } catch (e: Exception) {
            Log.e("ItemViewModel", "Error saving items to cache", e)
        }
    }

    private fun loadItemsFromCache(): List<Item> {
        return try {
            context.openFileInput(itemsCacheFileName).use { stream ->
                val jsonString = stream.bufferedReader().use { it.readText() }
                val gson = Gson()
                val type = object : TypeToken<List<Item>>() {}.type
                gson.fromJson(jsonString, type)
            }
        } catch (e: Exception) {
            Log.e("ItemViewModel", "Error loading items from cache", e)
            emptyList()
        }
    }

    private fun saveItemToCache(id: String, item: Item) {
        try {
            // Create the item_details directory if it doesn't exist
            val detailsDir = File(context.filesDir, itemDetailsCacheDir)
            if (!detailsDir.exists()) {
                detailsDir.mkdir()
            }

            val gson = Gson()
            val jsonString = gson.toJson(item)
            val file = File(detailsDir, "${id}.json")
            file.writeText(jsonString)
            Log.d("ItemViewModel", "Saved item details to cache: $id at ${file.absolutePath}")
            Log.d("ItemViewModel", "Cache contents: $jsonString")
        } catch (e: Exception) {
            Log.e("ItemViewModel", "Error saving item details to cache", e)
        }
    }

    private fun loadItemFromCache(id: String): Item? {
        return try {
            val detailsDir = File(context.filesDir, itemDetailsCacheDir)
            val file = File(detailsDir, "${id}.json")
            Log.d("ItemViewModel", "Attempting to load cache from: ${file.absolutePath}")
            
            if (file.exists()) {
                val jsonString = file.readText()
                Log.d("ItemViewModel", "Found cached content: $jsonString")
                val gson = Gson()
                gson.fromJson(jsonString, Item::class.java)
            } else {
                Log.d("ItemViewModel", "No cache file found for id: $id")
                null
            }
        } catch (e: Exception) {
            Log.e("ItemViewModel", "Error loading item details from cache", e)
            null
        }
    }

    // Function to fetch items from the API
    fun getItems() {
        viewModelScope.launch {
            try {
                val response = api.getItems()
                if (response.isSuccessful) {
                    response.body()?.let { rawResponse ->
                        try {
                            val gson = Gson()
                            val type = object : TypeToken<List<Item>>() {}.type
                            val items = gson.fromJson<List<Item>>(rawResponse, type)
                            _items.value = items
                            // Save to cache when we get new data
                            saveItemsToCache(items)
                            Log.d("ItemViewModel", "Successfully fetched and cached items")
                        } catch (e: Exception) {
                            Log.e("ItemViewModel", "Error parsing items JSON", e)
                            // Load from cache if parsing fails
                            _items.value = loadItemsFromCache()
                        }
                    }
                } else {
                    Log.e("ItemViewModel", "Error: ${response.code()} - ${response.message()}")
                    _items.value = loadItemsFromCache()
                }
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Error fetching items", e)
                _items.value = loadItemsFromCache()
            }
        }
    }

    fun getItemById(id: String) {
        viewModelScope.launch {
            try {
                // First try to load from cache
                val cachedItem = loadItemFromCache(id)
                if (cachedItem != null) {
                    _item.value = cachedItem
                    Log.d("ItemViewModel", "Successfully loaded item from cache: $id")
                } else {
                    Log.d("ItemViewModel", "No cached item found for id: $id")
                }

                // Then try to fetch from network
                try {
                    val response = api.getItemById(id)
                    
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Log.d("ItemViewModel", "API Response: $apiResponse")
                        
                        if (apiResponse != null) {
                            val itemData = apiResponse.data
                            val item = Item(
                                id = id,
                                brand = itemData.brand,
                                name = itemData.name,
                                category = itemData.category,
                                description = itemData.description,
                                quantity = itemData.quantity,
                                sale_price = itemData.sale_price,
                                rental_rate = itemData.rental_rate,
                                image = itemData.image,
                                image_url = itemData.image
                            )
                            _item.value = item
                            // Save to cache
                            saveItemToCache(id, item)
                            Log.d("ItemViewModel", "Successfully updated item from network: $id")
                        } else {
                            Log.e("ItemViewModel", "Response body was null for ID: $id")
                            if (_item.value == null && cachedItem != null) {
                                _item.value = cachedItem
                            }
                        }
                    } else {
                        Log.e("ItemViewModel", "Error response: ${response.code()} - ${response.message()}")
                        if (_item.value == null && cachedItem != null) {
                            _item.value = cachedItem
                        }
                    }
                } catch (e: Exception) {
                    Log.e("ItemViewModel", "Network error in getItemById", e)
                    if (_item.value == null && cachedItem != null) {
                        _item.value = cachedItem
                    }
                }
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Exception in getItemById", e)
                // If everything fails, try cache one last time
                if (_item.value == null) {
                    val cachedItem = loadItemFromCache(id)
                    _item.value = cachedItem
                }
            }
        }
    }

    fun searchItems(query: String) {
        viewModelScope.launch {
            if (query.isEmpty()) {
                _searchResults.value = emptyList()
                return@launch
            }

            try {
                // First try to use current items
                var itemsToSearch = items.value
                
                // If no items in memory, try to load from cache
                if (itemsToSearch.isEmpty()) {
                    itemsToSearch = loadItemsFromCache()
                }

                val filtered = itemsToSearch.filter { item ->
                    item.name.contains(query, ignoreCase = true) ||
                    item.brand.contains(query, ignoreCase = true) ||
                    item.category.contains(query, ignoreCase = true)
                }
                _searchResults.value = filtered
                Log.d("ItemViewModel", "Search completed with ${filtered.size} results")
            } catch (e: Exception) {
                Log.e("ItemViewModel", "Error searching items", e)
                _searchResults.value = emptyList()
            }
        }
    }
}
