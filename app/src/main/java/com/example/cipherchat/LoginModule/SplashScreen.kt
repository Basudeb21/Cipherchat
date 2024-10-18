package com.example.cipherchat.LoginModule


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.cipherchat.R

class SplashScreen : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_splash_screen, container, false)

        // Delay for 3 seconds before replacing the fragment
        Handler(Looper.getMainLooper()).postDelayed({
            // Create an instance of the SignupFragment to load
             // Replace this with your actual fragment class

            // Replace the current fragment with the new fragment
            val transaction: FragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, LoginScreen()) // Replace with your container ID
            transaction.addToBackStack(null) // Optional: adds the transaction to the back stack
            transaction.commit()
        }, 3000) // 3000 milliseconds = 3 seconds

        return view
    }
}
