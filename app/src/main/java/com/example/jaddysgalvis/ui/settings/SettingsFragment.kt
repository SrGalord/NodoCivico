package com.example.jaddysgalvis.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.jaddysgalvis.R

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var switchDarkMode: Switch

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchDarkMode = view.findViewById(R.id.switchDarkMode)

        val prefs = requireActivity()
            .getSharedPreferences("settings", Context.MODE_PRIVATE)

        val isDark = prefs.getBoolean("dark_mode", false)

        switchDarkMode.setOnCheckedChangeListener(null)
        switchDarkMode.isChecked = isDark

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->

            prefs.edit()
                .putBoolean("dark_mode", isChecked)
                .apply()

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}