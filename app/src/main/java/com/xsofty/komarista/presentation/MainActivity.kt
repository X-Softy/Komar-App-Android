package com.xsofty.komarista.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.firebase.FirebaseApp
import com.xsofty.komarista.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view)
        val displayedFragment = navHostFragment?.childFragmentManager?.fragments?.first()
        displayedFragment?.onActivityResult(requestCode, resultCode, data)
    }
}