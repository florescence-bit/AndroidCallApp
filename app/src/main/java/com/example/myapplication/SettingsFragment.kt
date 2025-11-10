package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSettings()
        setupListeners()
    }

    private fun loadSettings() {
        val prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        
        // Theme
        binding.themeModeSwitch.isChecked = prefs.getBoolean(KEY_DARK_MODE, false)
        
        // Notifications
        binding.callNotificationsSwitch.isChecked = prefs.getBoolean(KEY_CALL_NOTIFICATIONS, true)
        binding.missedCallNotificationsSwitch.isChecked = prefs.getBoolean(KEY_MISSED_CALL_NOTIFICATIONS, true)
        
        // Privacy
        binding.showContactPhotosSwitch.isChecked = prefs.getBoolean(KEY_SHOW_CONTACT_PHOTOS, true)
        binding.callHistoryPrivacySwitch.isChecked = prefs.getBoolean(KEY_HIDE_CALL_HISTORY, false)
    }

    private fun setupListeners() {
        binding.themeModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSettings(KEY_DARK_MODE, isChecked)
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        binding.callNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSettings(KEY_CALL_NOTIFICATIONS, isChecked)
        }

        binding.missedCallNotificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSettings(KEY_MISSED_CALL_NOTIFICATIONS, isChecked)
        }

        binding.showContactPhotosSwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSettings(KEY_SHOW_CONTACT_PHOTOS, isChecked)
        }

        binding.callHistoryPrivacySwitch.setOnCheckedChangeListener { _, isChecked ->
            saveSettings(KEY_HIDE_CALL_HISTORY, isChecked)
        }
    }

    private fun saveSettings(key: String, value: Boolean) {
        requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(key, value)
            .apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val KEY_DARK_MODE = "dark_mode"
        private const val KEY_CALL_NOTIFICATIONS = "call_notifications"
        private const val KEY_MISSED_CALL_NOTIFICATIONS = "missed_call_notifications"
        private const val KEY_SHOW_CONTACT_PHOTOS = "show_contact_photos"
        private const val KEY_HIDE_CALL_HISTORY = "hide_call_history"
    }
}