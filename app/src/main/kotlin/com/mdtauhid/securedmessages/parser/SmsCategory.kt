package com.mdtauhid.securedmessages.parser

enum class SmsCategory(val code: String) {

    PROMOTIONAL("P"),
    SERVICE("S"),
    GOVERNMENT("G"),
    TRANSACTIONAL("T"),
    IMPLICIT("I"),
    SPAM("U"),
    UNKNOWN("?");

    companion object {

        fun fromCode(code: String?): SmsCategory {
            return values().find { it.code == code } ?: UNKNOWN
        }

    }
}