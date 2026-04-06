package com.mdtauhid.securedmessages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.mdtauhid.securedmessages.database.DatabaseProvider
import com.mdtauhid.securedmessages.databinding.ActivityMainBinding
import com.mdtauhid.securedmessages.model.Message
import com.mdtauhid.securedmessages.parser.SmsCategory
import com.mdtauhid.securedmessages.repository.MessageRepository
import com.mdtauhid.securedmessages.ui.MessageAdapter
import com.mdtauhid.securedmessages.viewmodel.MessageViewModel
import com.mdtauhid.securedmessages.viewmodel.MessageViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MessageAdapter
    private var activeSource: LiveData<List<Message>>? = null

    private val viewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(
            MessageRepository(
                DatabaseProvider.getDatabase(this).messageDao()
            )
        )
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val receiveSms = permissions[Manifest.permission.RECEIVE_SMS] == true
        val readSms = permissions[Manifest.permission.READ_SMS] == true
        if (!receiveSms || !readSms) {
            Toast.makeText(
                this,
                getString(R.string.permissions_denied_message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFilters()
        requestSmsPermissionsIfNeeded()
        observeMessagesFor(null)
    }

    private fun requestSmsPermissionsIfNeeded() {
        val permissionsNeeded = arrayOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS
        ).filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (permissionsNeeded.isNotEmpty()) {
            permissionLauncher.launch(permissionsNeeded.toTypedArray())
        }
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupFilters() {
        binding.filterChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val selectedCategory = when (checkedIds.firstOrNull()) {
                R.id.chipPromotional -> SmsCategory.PROMOTIONAL
                R.id.chipService -> SmsCategory.SERVICE
                R.id.chipGovernment -> SmsCategory.GOVERNMENT
                R.id.chipTransactional -> SmsCategory.TRANSACTIONAL
                R.id.chipImplicit -> SmsCategory.IMPLICIT
                R.id.chipSpam -> SmsCategory.SPAM
                else -> null
            }
            observeMessagesFor(selectedCategory)
        }
    }

    private fun observeMessagesFor(category: SmsCategory?) {
        activeSource?.removeObservers(this)
        val source = if (category == null) {
            viewModel.allMessages
        } else {
            viewModel.getMessagesByCategory(category)
        }

        activeSource = source
        source.observe(this) { messages ->
            adapter.submitList(messages)
            binding.emptyStateText.visibility =
                if (messages.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }
    }
}
