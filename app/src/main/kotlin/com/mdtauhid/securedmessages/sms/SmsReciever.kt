package com.mdtauhid.securedmessages.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.mdtauhid.securedmessages.database.DatabaseProvider
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.parser.SmsCategoryParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {

            val pendingResult = goAsync()
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)

            val database = DatabaseProvider.getDatabase(context)
            val dao = database.messageDao()

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    for (sms in messages) {

                        val sender = sms.displayOriginatingAddress ?: "UNKNOWN"
                        val body = sms.messageBody ?: ""

                        val category = SmsCategoryParser.parse(sender)

                        val message = Message(
                            sender = sender,
                            body = body,
                            timestamp = System.currentTimeMillis(),
                            category = category
                        )

                        Log.d("SecuredMessages", "Sender: $sender")
                        Log.d("SecuredMessages", "Category: $category")
                        Log.d("SecuredMessages", "Message: $body")

                        dao.insert(message)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
