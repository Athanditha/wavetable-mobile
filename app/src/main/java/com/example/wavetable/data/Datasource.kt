package com.example.wavetable.data

import com.example.wavetable.R
import com.example.wavetable.model.Brand
import com.example.wavetable.model.Item
import com.example.wavetable.model.Notification
import com.example.wavetable.model.cartItem

class Datasource {
    fun loadCartItems(): List<cartItem> {
        return listOf<cartItem>(
            cartItem(R.string.title1, R.drawable.prod1, R.string.quan1, R.string.pricing1),
            cartItem(R.string.title4, R.drawable.prod4, R.string.quan2, R.string.pricing4),
            cartItem(R.string.title7, R.drawable.prod7, R.string.quan3, R.string.pricing7),
            cartItem(R.string.title8, R.drawable.prod8, R.string.quan4, R.string.pricing8)
        )
    }
    fun loadBrand(): List<Brand> {
        return listOf<Brand>(
            Brand(R.drawable.brand1, R.string.brand1),
            Brand(R.drawable.brand2, R.string.brand2),
            Brand(R.drawable.brand3, R.string.brand3),
            Brand(R.drawable.brand4, R.string.brand4),
            Brand(R.drawable.brand5, R.string.brand5),
            Brand(R.drawable.brand6, R.string.brand6),
            Brand(R.drawable.brand7, R.string.brand7),
            Brand(R.drawable.brand8, R.string.brand8),
            Brand(R.drawable.brand9, R.string.brand9),
        )
    }
}
