package com.example.jaddysgalvis.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(
            R.layout.fragment_splash,
            container,
            false
        )

        val content = view.findViewById<View>(R.id.contentSplash)

        val animation = AnimationUtils.loadAnimation(
            requireContext(),
            android.R.anim.fade_in
        )

        animation.duration = 1400

        content.startAnimation(animation)

        view.postDelayed({

            val prefs = requireActivity()
                .getSharedPreferences("session", android.content.Context.MODE_PRIVATE)

            val userId = prefs.getInt("user_id", -1)

            if (userId == -1) {
                findNavController().navigate(R.id.loginFragment)
            } else {
                findNavController().navigate(R.id.reportsFragment)
            }

        }, 2500)

        return view
    }
}