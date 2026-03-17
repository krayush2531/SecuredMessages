package com.mdtauhid.securedmessages.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.parser.SmsCategory
import com.mdtauhid.securedmessages.repository.MessageRepository
import kotlinx.coroutines.launch

class MessageViewModel(
    private val repository: MessageRepository
) : ViewModel() {

    val allMessages: LiveData<List<Message>> = repository.getAllMessages().asLiveData()

    fun getMessagesByCategory(category: SmsCategory): LiveData<List<Message>> {
        return repository.getMessagesByCategory(category).asLiveData()
    }

    fun insertMessage(message: Message) {
        viewModelScope.launch {
            repository.insertMessage(message)
        }
    }
}