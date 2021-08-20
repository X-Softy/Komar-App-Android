package com.xsofty.komarista.auth

import android.app.Activity
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.xsofty.komarista.R
import com.xsofty.shared.storage.AppPreferences

class FirebaseAuthHelper(
    private val activity: Activity,
    private val appPreferences: AppPreferences
) {

    private var mAuth = FirebaseAuth.getInstance()

    private var signInOptions: GoogleSignInOptions =
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

    private val googleSignInClient = GoogleSignIn.getClient(activity, signInOptions)

    fun isUserSignedIn(): Boolean {
        return mAuth.currentUser != null
    }

    fun fireSignInIntent() {
        activity.startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    fun signOut() {
        mAuth.signOut()
    }

    fun handleSignInIntent(data: Intent?) {
        val completedTask = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = completedTask.getResult(ApiException::class.java)
        try {
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            mAuth.signInWithCredential(credential).addOnCompleteListener(activity) { signInTask ->
                if (signInTask.isSuccessful) {
                    fetchIdToken(signInTask)
                } else {
                    sendErrorCallback()
                }
            }
        } catch (e: ApiException) {
            sendErrorCallback()
        }
    }

    private fun fetchIdToken(signInTask: Task<AuthResult>) {
        val user = signInTask.result?.user
        user?.getIdToken(true)?.addOnCompleteListener(activity) { idTokenTask ->
            if (idTokenTask.isSuccessful) {
                saveIdToken(idTokenTask.result?.token)
                sendSuccessCallback()
            } else {
                sendErrorCallback()
            }
        }
    }

    private fun saveIdToken(idToken: String?) {
        appPreferences.idToken = idToken
    }

    private fun sendSuccessCallback() {
        (activity as AuthResultListener).onAuthSucceeded()
    }

    private fun sendErrorCallback() {
        (activity as AuthResultListener).onAuthFailed()
    }

    companion object {
        const val RC_SIGN_IN = 753
    }
}