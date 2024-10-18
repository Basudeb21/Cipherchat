package com.example.cipherchat.LoginModule

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.cipherchat.ApplicationModule.ApplicationActivities
import com.example.cipherchat.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignupScreen : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var signupButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var logmein: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_signup_screen, container, false)

        // Initialize Firebase Auth and Database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize UI elements
        emailInput = view.findViewById(R.id.email_input)
        passwordInput = view.findViewById(R.id.password_input)
        signupButton = view.findViewById(R.id.signup_button)
        progressBar = view.findViewById(R.id.progress_bar)
        logmein = view.findViewById(R.id.log_me_in)

        // Set signup button click listener
        signupButton.setOnClickListener {
            registerUser()
        }
        logmein.setOnClickListener {
            // Replace with the fragment transaction to open LoginScreen
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, LoginScreen())  // Make sure R.id.fragment_container matches your container in activity XML
                .addToBackStack(null)  // Adds the transaction to the back stack, so user can navigate back
                .commit()
        }


        return view
    }

    private fun registerUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Validate input
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        signupButton.isEnabled = false

        // Create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                signupButton.isEnabled = true

                if (task.isSuccessful) {
                    // Registration successful
                    val user = task.result?.user
                    user?.let {
                        saveUserToDatabase(it.uid, email)
                    }
                } else {
                    // Registration failed
                    Toast.makeText(context, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToDatabase(userId: String, email: String) {
        val userMap = hashMapOf(
            "email" to email
        )

        // Save user data to Firebase Realtime Database
        database.reference.child("users").child(userId).setValue(userMap)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE // Always hide progress bar

                if (task.isSuccessful) {
                    Toast.makeText(context, "User data saved successfully", Toast.LENGTH_SHORT).show()

                    // Navigate to ApplicationModule after success
                    val intent = Intent(activity, ApplicationActivities::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Failed to save user data", Toast.LENGTH_SHORT).show()
                }
            }
    }
}