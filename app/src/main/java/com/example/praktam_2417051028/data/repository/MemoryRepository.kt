package com.example.praktam_2417051028.data.repository

import com.example.praktam_2417051028.data.api.RetrofitClient
import com.example.praktam_2417051028.lifereplay.Memory

class MemoryRepository {

    suspend fun getMemories(): List<Memory> {
        val response = RetrofitClient.api.getMemories()

        return when {
            response.isJsonArray -> {
                response.asJsonArray.mapNotNull { item ->
                    val obj = item.asJsonObject
                    Memory(
                        title = obj["title"]?.asString ?: "",
                        description = obj["description"]?.asString ?: "",
                        date = obj["date"]?.asString ?: "",
                        imageUrl = obj["imageUrl"]?.asString ?: ""
                    )
                }
            }

            response.isJsonObject -> {
                response.asJsonObject.entrySet().mapNotNull { entry ->
                    val obj = entry.value.asJsonObject
                    Memory(
                        title = obj["title"]?.asString ?: "",
                        description = obj["description"]?.asString ?: "",
                        date = obj["date"]?.asString ?: "",
                        imageUrl = obj["imageUrl"]?.asString ?: ""
                    )
                }
            }

            else -> emptyList()
        }
    }

    suspend fun addMemory(memory: Memory) {
        RetrofitClient.api.addMemory(memory)
    }
}