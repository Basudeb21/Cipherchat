package com.example.cipherchat.ApplicationModule

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.cipherchat.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import de.hdodenhof.circleimageview.CircleImageView

class ApplicationActivities : AppCompatActivity() {
    private lateinit var navbar: BottomNavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var u_name: TextView
    private lateinit var u_mail: TextView
    private lateinit var u_nname: TextView
    private lateinit var profilePic: CircleImageView
    private lateinit var databaseRef: DatabaseReference
    private lateinit var head_pic: CircleImageView

    private fun init() {
        navbar = findViewById(R.id.navbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.side_nav)
        val headerView = navView.getHeaderView(0)
        u_name = headerView.findViewById(R.id.user_name)
        u_mail = headerView.findViewById(R.id.user_mail)
        u_nname = headerView.findViewById(R.id.user_nick_name)
        head_pic = headerView.findViewById(R.id.head_profile_pic)
        // Uncomment and modify this line if needed
        // databaseRef = FirebaseDatabase.getInstance().getReference("estory/users/${FirebaseAuth.getInstance().currentUser?.uid}/profile/user_profile_pic")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary)
        setContentView(R.layout.activity_application_activities)

        // Initialize the views after setting the content view
        init()

        // Set up window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.frame_layout)) { v, insets ->  // Updated ID reference
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val menuButton: View = findViewById(R.id.menu)
        menuButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}
