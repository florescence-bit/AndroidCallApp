package com.example.myapplication

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val PERMISSIONS_REQUEST_READ_PROFILE = 101
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermissionAndLoadUserProfile()
        setupQuickAccessShortcuts()
    }

    private fun checkPermissionAndLoadUserProfile() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_PROFILE
            )
        } else {
            loadUserProfile()
        }
    }

    private fun loadUserProfile() {
        val cursor: Cursor? = requireContext().contentResolver.query(
            ContactsContract.Profile.CONTENT_URI,
            arrayOf(ContactsContract.Profile.DISPLAY_NAME),
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(ContactsContract.Profile.DISPLAY_NAME)
                if (nameIndex >= 0) {
                    val userName = it.getString(nameIndex)
                    binding.welcomeText.text = "Hello $userName"
                }
            }
        }
    }

    private fun setupQuickAccessShortcuts() {
        // Load frequently contacted people
        loadFrequentContacts()
        
        // Set up quick action buttons
        binding.apply {
            quickDialButton.setOnClickListener {
                // Navigate to dial pad
                // Implementation depends on your navigation setup
            }
            
            recentCallsButton.setOnClickListener {
                // Navigate to recent calls
                // Implementation depends on your navigation setup
            }
            
            favoritesButton.setOnClickListener {
                // Navigate to favorites
                // Implementation depends on your navigation setup
            }
        }
    }

    private fun loadFrequentContacts() {
        val cursor: Cursor? = requireContext().contentResolver.query(
            ContactsContract.Contacts.CONTENT_STREQUENT_URI,
            arrayOf(
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts._ID
            ),
            null,
            null,
            null
        )

        cursor?.use {
            // Process frequent contacts
            // Implementation depends on your UI layout
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_PROFILE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadUserProfile()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}