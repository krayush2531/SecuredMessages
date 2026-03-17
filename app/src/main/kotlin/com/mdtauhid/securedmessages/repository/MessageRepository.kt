package com.mdtauhid.securedmessages.repository

import com.mdtauhid.securedmessages.database.MessageDao
import com.mdtauhid.securedmessages.model.Message

class MessageRepository(private val messageDao: MessageDao) {

    suspend fun insertMessage(message: Message) {
        messageDao.insert(message)
    }

    suspend fun getAllMessages(): List<Message> {
        return messageDao.getAllMessages()
    }

    suspend fun getMessagesByCategory(category: String): List<Message> {
        return messageDao.getMessagesByCategory(category)
    }
}