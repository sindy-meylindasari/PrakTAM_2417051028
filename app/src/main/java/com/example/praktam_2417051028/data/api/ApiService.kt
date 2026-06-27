package com.example.praktam_2417051028.data.api

import com.example.praktam_2417051028.lifereplay.Memory
import com.google.gson.JsonElement
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("memories.json")
    suspend fun getMemories(): JsonElement

    @POST("memories.json")
    suspend fun addMemory(@Body memory: Memory)
}