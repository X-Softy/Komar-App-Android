package com.xsofty.komarista.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseUser
import com.xsofty.komarista.auth.AuthHelper
import com.xsofty.komarista.ui.theme.KomaristaTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.foundation.Image
import com.google.accompanist.glide.rememberGlidePainter

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private lateinit var authHelper: AuthHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        authHelper = AuthHelper.get(this)

        setContent {
            KomaristaTheme {
                authLayout()
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    private fun authLayout() {

        val account: FirebaseUser? by authHelper.user.observeAsState()

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
        ) {
            Image(
                painter = rememberGlidePainter(
                    request = account?.photoUrl?: "https://picsum.photos/300/300",
                ),
                contentDescription = ""
            )
            Text(text = account?.email ?: "")
            Text(text = account?.displayName ?: "")
            Button(onClick = { authHelper.signIn() }) {
                Text(text = "Sign In")
            }
            Button(onClick = { authHelper.signOut() }) {
                Text(text = "Sign Out")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) {
            // TODO: Handle cancelled
            finish()
            return
        }

        if (requestCode == RC_SIGN_IN) {
            authHelper.handleSuccessfulSignIn(data)
        }
    }

    companion object {
        const val RC_SIGN_IN = 123
    }
}