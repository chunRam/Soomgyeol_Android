package com.example.meditation.model

data class JournalEntry(
    val content: String = "",
    val timestamp: com.google.firebase.Timestamp? = null,
    val duration_minutes: Int = 0
)
