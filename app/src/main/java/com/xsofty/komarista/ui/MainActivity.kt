package com.xsofty.komarista.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.firebase.FirebaseApp
import com.xsofty.komarista.R

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment)
        val displayedFragment = navHostFragment?.childFragmentManager?.fragments?.first()
        displayedFragment?.onActivityResult(requestCode, resultCode, data)
    }
}