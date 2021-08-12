package com.xsofty.categories.presentation

import androidx.core.net.toUri
import androidx.navigation.NavController
import com.xsofty.shared.nav.DeepLinkHandler
import com.xsofty.shared.nav.contracts.CategoriesNavContract

class CategoriesNavImpl : CategoriesNavContract {

    override fun show(navController: NavController) {
        navController.navigate(
            DeepLinkHandler.CATEGORIES.toUri()
        )
    }
}