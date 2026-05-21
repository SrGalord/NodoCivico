package com.example.jaddysgalvis

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.jaddysgalvis.data.local.database.AppDatabase
import com.example.jaddysgalvis.data.local.entity.UserEntity
import com.example.jaddysgalvis.data.session.SessionManager
import com.example.jaddysgalvis.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        // 🌙 DARK MODE
        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDark) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
                    as NavHostFragment

        val navController = navHost.navController

        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.splashFragment,
                R.id.loginFragment,
                R.id.registerFragment -> {
                    binding.bottomNavigation.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                }
            }
        }

        // 🔥 CREAR ADMIN AUTOMÁTICO
        createDefaultAdmin()

        // 🔥 DEBUG SESSION
        val sessionUserId = SessionManager.getUserId(this)
        val sessionRole = SessionManager.getUserRole(this)

        println("SESSION -> ID: $sessionUserId ROLE: $sessionRole")
    }

    // 🔥 ADMIN SEED (CORREGIDO)
    private fun createDefaultAdmin() {

        lifecycleScope.launch {

            val db = AppDatabase.getDatabase(this@MainActivity)

            val adminExists = db.userDao().getAdmin()

            if (adminExists == null) {

                val admin = UserEntity(
                    name = "Admin",
                    email = "admin@gmail.com",
                    password = "1234",
                    role = "ADMIN"
                )

                db.userDao().registerUser(admin)

                println("🔥 ADMIN CREADO AUTOMÁTICAMENTE")

            } else {

                println("✅ ADMIN YA EXISTE")
            }
        }
    }
}