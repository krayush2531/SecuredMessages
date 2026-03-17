package com.mdtauhid.securedmessages

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.mdtauhid.securedmessages.database.DatabaseProvider
import com.mdtauhid.securedmessages.databinding.ActivityMainBinding
import com.mdtauhid.securedmessages.repository.MessageRepository
import com.mdtauhid.securedmessages.ui.MessagesPagerAdapter
import com.mdtauhid.securedmessages.viewmodel.MessageViewModel
import com.mdtauhid.securedmessages.viewmodel.MessageViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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

        requestSmsPermissionsIfNeeded()
        setupTabs()
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

    private fun setupTabs() {
        val pagerAdapter = MessagesPagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = pagerAdapter.tabs[position].title
        }.attach()
    }
}
