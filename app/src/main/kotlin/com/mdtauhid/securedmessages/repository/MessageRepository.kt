package com.mdtauhid.securedmessages.repository

import com.mdtauhid.securedmessages.database.MessageDao
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.parser.SmsCategory
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {

    suspend fun insertMessage(message: Message) {
        messageDao.insert(message)
    }

    fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAllMessages()
    }

    fun getMessagesByCategory(category: SmsCategory): Flow<List<Message>> {
        return messageDao.getMessagesByCategory(category)
    }
}