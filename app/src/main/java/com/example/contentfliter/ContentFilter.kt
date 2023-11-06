package com.example.contentfliter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class ContentFilter {

    private val client = OkHttpClient()

    suspend fun isContentBlocked(url: String, violenceFilter: Boolean, explicitContentFilter: Boolean): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val content = response.body?.string() ?: ""
                    response.close()

                    val blockedViolenceKeywords = listOf("violent", "dangerous", "harmful")
                    val blockedExplicitKeywords = listOf("explicit", "adult", "pornographic")

                    if (violenceFilter && containsBlockedKeywords(content, blockedViolenceKeywords)) {
                        return@withContext true
                    }

                    if (explicitContentFilter && containsBlockedKeywords(content, blockedExplicitKeywords)) {
                        return@withContext true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return@withContext false
        }
    }

    private fun containsBlockedKeywords(text: String, blockedKeywords: List<String>): Boolean {
        for (keyword in blockedKeywords) {
            if (text.contains(keyword, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}
