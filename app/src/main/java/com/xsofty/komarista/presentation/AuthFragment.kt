package com.xsofty.komarista.presentation

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.fragment.findNavController
import com.google.accompanist.glide.rememberGlidePainter
import com.google.firebase.auth.FirebaseUser
import com.xsofty.komarista.R
import com.xsofty.komarista.auth.AuthHelper
import com.xsofty.shared.base.BaseFragment
import com.xsofty.shared.storage.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : BaseFragment() {

    private lateinit var authHelper: AuthHelper

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authHelper = AuthHelper.get(appPreferences, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
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
                .fillMaxHeight()
        ) {
            Image(
                painter = rememberGlidePainter(
                    request = account?.photoUrl
                        ?: "https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_1280.png",
                ),
                contentDescription = ""
            )
            Text(text = "Email: ${account?.email ?: "null"}")
            Text(text = "Name: ${account?.displayName ?: "null"}")
            Button(
                onClick = { authHelper.signIn() },
                enabled = account == null
            ) {
                Text(text = "Sign In")
            }
            Button(
                onClick = { authHelper.signOut() },
                enabled = account != null
            ) {
                Text(text = "Sign Out")
            }
            Button(
                onClick = {
                    findNavController().navigate(R.id.action_authFragment_to_categories_graph)
                },
                enabled = account != null
            ) {
                Text(text = "Navigate to categories")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_CANCELED) {
            // TODO: Handle cancelled
            requireActivity().finish()
            return
        }

        if (requestCode == RC_SIGN_IN) {
            authHelper.signInFromIntent(data)
        }
    }

    companion object {
        const val RC_SIGN_IN = 123
    }
}