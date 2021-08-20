package com.xsofty.komarista

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.xsofty.komarista.auth.AuthResultListener
import com.xsofty.komarista.auth.FirebaseAuthHelper
import com.xsofty.shared.nav.CustomBackPressable
import com.xsofty.shared.nav.BottomNavigationHandler
import com.xsofty.shared.nav.contracts.CategoriesNavContract
import com.xsofty.shared.nav.contracts.SignInNavContract
import com.xsofty.shared.storage.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavigationHandler, AuthResultListener {

    @Inject
    lateinit var appPreferences: AppPreferences

    @Inject
    lateinit var signInNavContract: SignInNavContract

    @Inject
    lateinit var categoriesNavContract: CategoriesNavContract

    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var authHelper: FirebaseAuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        authHelper = FirebaseAuthHelper(this, appPreferences)
        setupNavigation()

        Handler(Looper.getMainLooper()).postDelayed({
            if (authHelper.isUserSignedIn()) {
                navigateToCategories()
            } else {
                navigateToSignIn()
            }
        }, 2000L)
    }

    private fun setupNavigation() {
        bottomNavView = findViewById(R.id.bottom_nav_view)
        bottomNavView.setupWithNavController(getNavController())

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
        setupActionBarWithNavController(getNavController(), appBarConfiguration)
    }

    override fun onAuthSucceeded() {
        navigateToCategories()
    }

    override fun onAuthFailed() {
        // TODO: Show auth Error
    }

    override fun onBackPressed() {
        (getDisplayedFragment() as? CustomBackPressable)?.onBackPressed() ?: super.onBackPressed()
    }

    override fun showNavigation() {
        bottomNavView.visibility = View.VISIBLE
    }

    override fun hideNavigation() {
        bottomNavView.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        getDisplayedFragment()?.onActivityResult(requestCode, resultCode, data)
    }

    private fun navigateToSignIn() {
        signInNavContract.show(getNavController())
    }

    private fun navigateToCategories() {
        categoriesNavContract.show(getNavController())
    }

    private fun getNavController(): NavController {
        return Navigation.findNavController(this, R.id.fragment_container)
    }

    private fun getDisplayedFragment(): Fragment? {
        val fragmentContainer = supportFragmentManager.findFragmentById(R.id.fragment_container)
        return fragmentContainer?.childFragmentManager?.fragments?.first()
    }
}