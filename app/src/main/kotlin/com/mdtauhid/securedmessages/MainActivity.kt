package com.mdtauhid.securedmessages

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdtauhid.securedmessages.database.DatabaseProvider
import com.mdtauhid.securedmessages.databinding.ActivityMainBinding
import com.mdtauhid.securedmessages.ui.MessageAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.messageRecyclerView.layoutManager = LinearLayoutManager(this)

        loadMessages()
    }

    private fun loadMessages() {

        CoroutineScope(Dispatchers.IO).launch {

            val database = DatabaseProvider.getDatabase(this@MainActivity)
            val messages = database.messageDao().getAllMessages()

            withContext(Dispatchers.Main) {

                val adapter = MessageAdapter(messages)
                binding.messageRecyclerView.adapter = adapter

            }
        }
    }
}