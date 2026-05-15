package com.example.jaddysgalvis.ui.splash

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.jaddysgalvis.R

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        view.postDelayed({
            findNavController().navigate(R.id.homeFragment)
        }, 1500)

        return view
    }
}