package com.example.wavetable.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Item(@StringRes val stringResourceId: Int,
                @DrawableRes val brandImgResID:Int,
                @StringRes val brandTxtID:Int,
                @StringRes val ItemDescription:Int,
                @DrawableRes val imageResourceId: Int,
                @StringRes val priceItemID:Int
)
