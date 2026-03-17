package com.mdtauhid.securedmessages.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mdtauhid.securedmessages.model.Message

@Database(
    entities = [Message::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class MessageDatabase : RoomDatabase() {

    abstract fun messageDao(): MessageDao

}