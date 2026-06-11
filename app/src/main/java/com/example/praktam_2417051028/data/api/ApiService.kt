package com.example.praktam_2417051028.data.api

import com.example.praktam_2417051028.lifereplay.Memory
import retrofit2.http.GET

interface ApiService {
    @GET("memories.json")
    suspend fun getMemories(): List<Memory>
}