package com.example.praktam_2417051028.data.repository

import com.example.praktam_2417051028.data.api.RetrofitClient
import com.example.praktam_2417051028.lifereplay.Memory

class MemoryRepository {

    suspend fun getMemories(): List<Memory> {
        return RetrofitClient.api.getMemories()
    }
}