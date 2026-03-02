package com.example.praktam_2417051028.lifereplay

import androidx.annotation.DrawableRes

data class Memory(
    val title: String,
    val description: String,
    val date: String,
    @DrawableRes val imageRes: Int
)