package com.xsofty.komarista

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.xsofty.komarista.helper.AuthResultListener
import com.xsofty.komarista.helper.FirebaseAuthHelper
import com.xsofty.komarista.helper.FirebaseAuthHelper.Companion.RC_SIGN_IN
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.CategoriesNavContract
import com.xsofty.shared.storage.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AuthResultListener {

    @Inject
    lateinit var appPreferences: AppPreferences

    private val authHelper by lazy {
        FirebaseAuthHelper(this, appPreferences)
    }

    @Inject
    lateinit var categoriesNavContract: CategoriesNavContract

    private lateinit var bottomNavView: BottomNavigationView

    private val navController by lazy { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        setupNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        authHelper.signOut()
    }

    private fun setupNavigation() {
        bottomNavView = findViewById(R.id.bottom_nav_view)
        bottomNavView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment,
                R.id.roomsFragment,
                R.id.createRoomFragment,
                R.id.roomDetailsFragment -> {
                    bottomNavView.visibility = View.GONE
                }
                else -> {
                    bottomNavView.visibility = View.VISIBLE
                }
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.signInFragment,
                R.id.categoriesFragment,
                R.id.myRoomsFragment,
                R.id.roomsFragment,
                R.id.createRoomFragment,
                R.id.roomDetailsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun fireSignInIntent() {
        authHelper.fireSignInIntent()
    }

    override fun onAuthSucceeded() {
        navigateToCategories()
    }

    override fun onAuthFailed() {
        TODO("Not yet implemented")
    }

    override fun onBackPressed() {
        (getDisplayedFragment() as? CustomBackPressable)?.onBackPressed() ?: super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            onAuthFailed()
            return
        }

        if (requestCode == RC_SIGN_IN) {
            authHelper.handleSignInIntent(data)
        }
    }

    private fun navigateToCategories() {
        categoriesNavContract.show(navController)
    }

    private fun getDisplayedFragment(): Fragment? {
        val fragmentContainer = supportFragmentManager.findFragmentById(R.id.nav_host)
        return fragmentContainer?.childFragmentManager?.fragments?.first()
    }
}