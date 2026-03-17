package com.mdtauhid.securedmessages.database

import androidx.room.TypeConverter
import com.mdtauhid.securedmessages.parser.SmsCategory

class Converters {

    @TypeConverter
    fun fromCategory(category: SmsCategory): String {
        return category.name
    }

    @TypeConverter
    fun toCategory(value: String): SmsCategory {
        return SmsCategory.valueOf(value)
    }

}