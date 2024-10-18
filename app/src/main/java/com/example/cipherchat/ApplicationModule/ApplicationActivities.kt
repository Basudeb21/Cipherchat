package com.example.cipherchat.ApplicationModule

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.cipherchat.LoginModule.LoginActivity
import com.example.cipherchat.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView

class ApplicationActivities : AppCompatActivity() {
    private lateinit var navbar: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var u_name: TextView
    private lateinit var u_mail: TextView
    private lateinit var u_nname: TextView
    private lateinit var profilePic: CircleImageView
    private lateinit var head_pic: CircleImageView

    // Initialize SharedPreferences
    private lateinit var sharedPref: SharedPreferences

    private fun init() {
        navbar = findViewById(R.id.navbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.side_nav)
        val headerView = navView.getHeaderView(0)
        u_name = headerView.findViewById(R.id.user_name)
        u_mail = headerView.findViewById(R.id.user_mail)
        u_nname = headerView.findViewById(R.id.user_nick_name)
        head_pic = headerView.findViewById(R.id.head_profile_pic)

        // Initialize SharedPreferences
        sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        setContentView(R.layout.activity_application_activities)

        // Initialize the views after setting the content view
        init()

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set up menu button click listener for opening side navigation drawer
        val menuButton: View = findViewById(R.id.menu)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        // Handle Bottom Navigation item selection
        navbar.setOnItemSelectedListener { item ->
            when (item.itemId) {

                R.id.logout -> {
                    logoutUser()  // Trigger logout when the logout button is pressed
                    true
                }
                else -> false
            }
        }

        // Handle Side Navigation item selection
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.side_friends -> { // Replace with your actual chat button ID
                    openChatFragment()
                    true
                }
                R.id.side_logout -> {  // Assuming your side nav logout item has this ID
                    logoutUser()  // Trigger logout when the logout button is pressed
                    true
                }
                else -> false
            }
        }
    }
    private fun openChatFragment() {
        val chatFragment = ChatFragment()
        replaceFragment(chatFragment)
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)  // Replace with the correct container ID
        transaction.addToBackStack(null)  // Optional: adds the transaction to the back stack
        transaction.commit()
    }

    // Method to handle user logout
    private fun logoutUser() {
        FirebaseAuth.getInstance().signOut()  // Sign out from FirebaseAuth

        // Update SharedPreferences to set "isLoggedIn" to false
        val editor = sharedPref.edit()
        editor.putBoolean("isLoggedIn", false)
        editor.apply()

        // Start the LoginActivity and finish the current activity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Close the ApplicationActivities to prevent going back to it
    }
}
