package com.example.wavetable.model

import android.media.Image
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import android.util.Log

data class Item(
    val id: String = "",
    val brand: String = "",
    val name: String = "",
    val category: String = "",
    val description: String = "",
    val quantity: String = "",
    val sale_price: String = "",
    val rental_rate: String = "",
    val image: String = "",
    val image_url: String = ""
) {
    init {
        // Add validation logging
        if (id.isEmpty()) {
            Log.w("Item", "Created Item with empty ID")
        }
    }
}
