package com.mdtauhid.securedmessages.database

import android.content.Context
import androidx.room.Room

class DatabaseProvider {

    companion object {

        @Volatile
        private var INSTANCE: MessageDatabase? = null

        fun getDatabase(context: Context): MessageDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageDatabase::class.java,
                    "secured_messages_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }

    }
}