package com.mdtauhid.securedmessages.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.parser.SmsCategory

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: Message)

    @Query("SELECT * FROM Message ORDER BY timestamp DESC")
    suspend fun getAllMessages(): List<Message>

    @Query("SELECT * FROM Message WHERE category = :category ORDER BY timestamp DESC")
    suspend fun getMessagesByCategory(category: SmsCategory): List<Message>

}