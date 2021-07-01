package com.xsofty.komarista.auth

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.xsofty.komarista.R
import com.xsofty.komarista.ui.MainActivity
import timber.log.Timber

class AuthHelper(
    private val context: Activity,
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
        context.startActivityForResult(signInIntent, MainActivity.RC_SIGN_IN)
    }

    fun signOut() {
        _user.value = null
        mAuth.signOut()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun onCreate() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        signInClient = GoogleSignIn.getClient(context, gso);
    }

    fun handleSuccessfulSignIn(data: Intent?) {
        val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = completedTask.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            mAuth.signInWithCredential(credential)
                .addOnCompleteListener(
                    context
                ) { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser
                        _user.value = user
                        Toast.makeText(context, "Signed In!", Toast.LENGTH_SHORT).show()
                        Timber.d("Here %s", user.toString())
                    } else {
                        _user.value = null
                        Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: ApiException) {

        }
    }

    companion object {
        fun get(activity: FragmentActivity): AuthHelper {
            return AuthHelper(activity, activity)
        }
    }
}