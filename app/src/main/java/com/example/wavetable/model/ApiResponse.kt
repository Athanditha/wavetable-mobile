package com.example.wavetable.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ItemData
)

data class ItemData(
    @SerializedName("brand") val brand: String = "",
    @SerializedName("category") val category: String = "",
    @SerializedName("description") val description: String = "",
    @SerializedName("image") val image: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("quantity") val quantity: String = "",
    @SerializedName("rental_rate") val rental_rate: String = "",
    @SerializedName("sale_price") val sale_price: String = ""
) 