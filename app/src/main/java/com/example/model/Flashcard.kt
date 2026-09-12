package com.example.model

data class Flashcard(
    val id: String = System.currentTimeMillis().toString(),
    val subject: String,
    val concept: String,
    val explanation: String,
    val isCustom: Boolean = false
)

data class ChatMessage(
    val id: String = System.currentTimeMillis().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String? = null
)

enum class MessageSender {
    USER, AI
}

data class ChatThread(
    val id: String = System.currentTimeMillis().toString(),
    val title: String,
    val lastMessage: String,
    val timestamp: Long = System.currentTimeMillis()
)
