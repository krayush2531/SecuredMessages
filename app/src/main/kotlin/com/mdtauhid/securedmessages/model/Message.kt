package com.mdtauhid.securedmessages.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mdtauhid.securedmessages.parser.SmsCategory

@Entity
data class Message(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val sender: String,

    val body: String,

    val timestamp: Long,

    val category: SmsCategory

)