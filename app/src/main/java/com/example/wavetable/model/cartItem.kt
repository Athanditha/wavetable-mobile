package com.example.wavetable.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class cartItem(
    @StringRes val CartItemId: Int,
    @DrawableRes val CartItemImage: Int,
    @StringRes val CartItemQuantity: Int,
    @StringRes val CartItemPrice: Int,

)
