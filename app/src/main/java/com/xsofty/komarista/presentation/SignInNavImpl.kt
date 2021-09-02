package com.xsofty.komarista.presentation

import androidx.core.net.toUri
import androidx.navigation.NavController
import com.xsofty.shared.nav.DeepLinkHandler
import com.xsofty.shared.nav.contracts.SignInNavContract

class SignInNavImpl : SignInNavContract {

    override fun show(navController: NavController) {
        navController.navigate(
            DeepLinkHandler.SIGN_IN.toUri()
        )
    }
}