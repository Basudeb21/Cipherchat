package com.example.cipherchat.LoginModule

import android.content.Intent
import android.content.SharedPreferences
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

class LoginScreen : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var signMeUp: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_screen, container, false)

        // Check if the user is already logged in
        val sharedPref = requireActivity().getSharedPreferences("LoginPrefs", 0)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // If logged in, directly go to the main activity
            val intent = Intent(activity, ApplicationActivities::class.java)
            startActivity(intent)
            requireActivity().finish()
            return null  // Do not show the login screen
        }

        auth = FirebaseAuth.getInstance()

        // Initialize UI elements
        emailInput = view.findViewById(R.id.email_input)
        passwordInput = view.findViewById(R.id.password_input)
        loginButton = view.findViewById(R.id.create_account_btn)
        progressBar = view.findViewById(R.id.progress_bar)
        signMeUp = view.findViewById(R.id.sign_me_up)

        // Set login button click listener
        loginButton.setOnClickListener {
            loginUser()
        }

        // Navigate to sign up screen
        signMeUp.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, SignupScreen())  // Update with correct fragment id
                .addToBackStack(null)
                .commit()
        }

        return view
    }

    private fun loginUser() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return
        }

        progressBar.visibility = View.VISIBLE
        loginButton.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE
                loginButton.isEnabled = true

                if (task.isSuccessful) {
                    Toast.makeText(context, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Save login state in SharedPreferences
                    val sharedPref = requireActivity().getSharedPreferences("LoginPrefs", 0)
                    val editor = sharedPref.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.putString("userEmail", email)  // Optionally store the user email
                    editor.apply()

                    // Navigate to the main activity
                    val intent = Intent(activity, ApplicationActivities::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Optional logout method if you need to log the user out

}