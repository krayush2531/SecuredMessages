package com.mdtauhid.securedmessages.parser

class SmsCategoryParser {

    companion object {

        fun parse(sender: String?): SmsCategory {

            if (sender.isNullOrBlank()) {
                return SmsCategory.UNKNOWN
            }

            // Example sender: VA-MYCLQ-P
            val parts = sender.split("-")

            if (parts.size < 2) {
                return SmsCategory.UNKNOWN
            }

            val categoryCode = parts.last()

            return SmsCategory.fromCode(categoryCode)
        }

    }

}