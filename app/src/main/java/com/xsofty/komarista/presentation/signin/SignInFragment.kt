package com.xsofty.komarista.presentation.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.xsofty.komarista.auth.AuthResultListener
import com.xsofty.komarista.auth.FirebaseAuthHelper
import com.xsofty.komarista.auth.FirebaseAuthHelper.Companion.RC_SIGN_IN
import com.xsofty.shared.storage.AppPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    @Inject
    lateinit var appPreferences: AppPreferences

    private lateinit var authHelper: FirebaseAuthHelper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Button(onClick = { authHelper.fireSignInIntent() }) {
                        Text(text = "Sign In")
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authHelper = FirebaseAuthHelper(requireActivity(), appPreferences)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_CANCELED) {
            (requireActivity() as AuthResultListener).onAuthFailed()
            return
        }

        if (requestCode == RC_SIGN_IN) {
            authHelper.handleSignInIntent(data)
        }
    }
}