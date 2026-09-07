package com.example.gigshield.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(text = "Hello! I am your on-device AI assistant. How can I help you today?", isUser = false)
    ),
    val inputText: String = ""
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputTextChanged(newText: String) {
        _uiState.update { it.copy(inputText = newText) }
    }

    fun sendMessage() {
        val currentText = _uiState.value.inputText.trim()
        if (currentText.isEmpty()) return

        val userMessage = ChatMessage(text = currentText, isUser = true)
        
        _uiState.update { state ->
            state.copy(
                messages = state.messages + userMessage,
                inputText = ""
            )
        }

        // Mock AI response
        simulateAiResponse()
    }

    private fun simulateAiResponse() {
        val mockResponse = ChatMessage(text = "I'm processing that on-device. This is a placeholder response.", isUser = false)
        _uiState.update { state ->
            state.copy(messages = state.messages + mockResponse)
        }
    }
}
