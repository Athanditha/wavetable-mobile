package com.example.wavetable

import android.content.Context
import com.example.wavetable.model.Item
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.http.Path
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import retrofit2.http.Headers
import com.example.wavetable.model.ApiResponse

interface ApiService{
    @GET("api/items")
    suspend fun getItems(): Response<String>

    @GET("api/items/{id}")
    @Headers("Accept: application/json")
    suspend fun getItemById(@Path("id") itemId: String): Response<ApiResponse>
}
