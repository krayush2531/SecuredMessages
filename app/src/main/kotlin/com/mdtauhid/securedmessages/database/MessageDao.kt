package com.mdtauhid.securedmessages.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.parser.SmsCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert
    suspend fun insert(message: Message)

    @Query("SELECT * FROM Message ORDER BY timestamp DESC")
    fun getAllMessages(): Flow<List<Message>>

    @Query("SELECT * FROM Message WHERE category = :category ORDER BY timestamp DESC")
    fun getMessagesByCategory(category: SmsCategory): Flow<List<Message>>

}