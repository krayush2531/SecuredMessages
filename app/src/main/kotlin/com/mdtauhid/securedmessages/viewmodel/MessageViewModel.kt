package com.mdtauhid.securedmessages.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.repository.MessageRepository
import kotlinx.coroutines.launch

class MessageViewModel(
    private val repository: MessageRepository
) : ViewModel() {

    fun insertMessage(message: Message) {
        viewModelScope.launch {
            repository.insertMessage(message)
        }
    }

    suspend fun getAllMessages(): List<Message> {
        return repository.getAllMessages()
    }

    suspend fun getMessagesByCategory(category: String): List<Message> {
        return repository.getMessagesByCategory(category)
    }

}