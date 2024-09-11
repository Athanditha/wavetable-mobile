package com.example.wavetable.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Brand(@DrawableRes val brandImgResID:Int,
                @StringRes val brandTxtID:Int,
)
