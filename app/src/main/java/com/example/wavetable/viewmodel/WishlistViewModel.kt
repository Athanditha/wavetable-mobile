package com.example.wavetable.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wavetable.ApiService
import com.example.wavetable.model.Item
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.logging.HttpLoggingInterceptor

class WishlistViewModel : ViewModel() {
    private val _wishlistItems = MutableStateFlow<List<Item>>(emptyList())
    val wishlistItems: StateFlow<List<Item>> = _wishlistItems

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val api = Retrofit.Builder()
        .baseUrl("https://6942-112-134-186-245.ngrok-free.app/")
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
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Accept", "application/json")
                    .method(original.method, original.body)
                    .build()
                Log.d("WishlistViewModel", "Request URL: ${request.url}")
                chain.proceed(request)
            }
            .addInterceptor(loggingInterceptor)
            .build())
        .build()
        .create(ApiService::class.java)

    init {
        fetchWishlistItems()
    }

    private fun fetchWishlistItems() {
        viewModelScope.launch {
            try {
                Log.d("WishlistViewModel", "Starting API call...")
                val response = api.getItems()  // Using the same endpoint for now
                
                if (response.isSuccessful) {
                    val rawResponse = response.body()
                    Log.d("WishlistViewModel", "Raw response: $rawResponse")
                    
                    if (!rawResponse.isNullOrEmpty()) {
                        try {
                            val gson = Gson()
                            val itemType = object : TypeToken<List<Item>>() {}.type
                            val items = gson.fromJson<List<Item>>(rawResponse, itemType)
                            _wishlistItems.value = items
                            Log.d("WishlistViewModel", "Successfully parsed items")
                        } catch (e: Exception) {
                            Log.e("WishlistViewModel", "Error parsing JSON: ${e.message}")
                            Log.e("WishlistViewModel", "Raw response was: $rawResponse")
                            _wishlistItems.value = emptyList()
                        }
                    } else {
                        Log.e("WishlistViewModel", "Empty response")
                        _wishlistItems.value = emptyList()
                    }
                } else {
                    Log.e("WishlistViewModel", "Error: ${response.code()} - ${response.message()}")
                    Log.e("WishlistViewModel", "Error body: ${response.errorBody()?.string()}")
                    _wishlistItems.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("WishlistViewModel", "Error fetching wishlist items", e)
                Log.e("WishlistViewModel", "Detailed error: ${e.message}")
                e.printStackTrace()
                _wishlistItems.value = emptyList()
            }
        }
    }


}



