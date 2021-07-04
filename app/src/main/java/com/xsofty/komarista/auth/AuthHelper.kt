package com.xsofty.komarista.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.xsofty.komarista.R
import com.xsofty.komarista.ui.AuthFragment.Companion.RC_SIGN_IN

class AuthHelper(
    private val mainActivity: Activity,
    lifecycleOwner: LifecycleOwner
) : LifecycleObserver {

    private lateinit var gso: GoogleSignInOptions
    private lateinit var signInClient: GoogleSignInClient
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableLiveData<FirebaseUser?>(null)
    val user: LiveData<FirebaseUser?> = _user

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun signIn() {
        val signInIntent = signInClient.signInIntent
        mainActivity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun signOut() {
        _user.value = null
        mAuth.signOut()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(mainActivity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(mainActivity, gso);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun tryToSignInAutomatically() {
        val account = GoogleSignIn.getLastSignedInAccount(mainActivity)
        if (account == null) {
            Toast.makeText(mainActivity, "Account is null", Toast.LENGTH_SHORT).show()
        } else {
            handleSuccessfulSignIn(account)
        }
    }

    fun signInFromIntent(data: Intent?) {
        val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = completedTask.getResult(ApiException::class.java)
        handleSuccessfulSignIn(account)
    }

    // can be called on start automatically
    private fun handleSuccessfulSignIn(account: GoogleSignInAccount?) {
        try {
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(
                    mainActivity
                ) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        _user.value = user
                        Toast.makeText(mainActivity, "Signed In!", Toast.LENGTH_SHORT).show()
                    } else {
                        _user.value = null
                        Toast.makeText(mainActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {

        }
    }

    companion object {
        fun get(fragment: Fragment): AuthHelper {
            return AuthHelper(fragment.requireActivity(), fragment.requireActivity())
        }
    }
}