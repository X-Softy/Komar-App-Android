package com.xsofty.komarista

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.xsofty.shared.nav.AuthResultListener
import com.xsofty.komarista.helper.FirebaseAuthHelper
import com.xsofty.komarista.helper.FirebaseAuthHelper.Companion.RC_SIGN_IN
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.contracts.CategoriesNavContract
import com.xsofty.shared.storage.AppPreferences
import com.xsofty.shared.theme.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import android.widget.Toolbar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.ui.NavigationUI
import com.xsofty.shared.theme.ColorType
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AuthResultListener {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var categoriesNavContract: CategoriesNavContract

    @Inject
    lateinit var themeManager: ThemeManager

    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val authHelper by lazy {
        FirebaseAuthHelper(this, appPreferences)
    }

    private val navController by lazy { findNavController(R.id.nav_host) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar?.setBackgroundDrawable(ColorDrawable(themeManager.primaryColorId))

        FirebaseApp.initializeApp(this)
        setupNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        authHelper.signOut()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        finish()
    }

    private fun setupNavigation() {
        bottomNavView = findViewById(R.id.bottom_nav_view)
        bottomNavView.setupWithNavController(navController)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.signInFragment,
                R.id.categoriesFragment,
                R.id.myRoomsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun fireSignInIntent() {
        authHelper.fireSignInIntent()
    }

    override fun onAuthSucceeded() {
        navigateToCategories()
    }

    override fun onAuthFailed() {
    }

    override fun signOut() {
        authHelper.signOut()
        finish()
    }

//    override fun onBackPressed() {
//        (getDisplayedFragment() as? CustomBackPressable)?.onBackPressed() ?: super.onBackPressed()
//    }

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