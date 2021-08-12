package com.xsofty.rooms.presentation.rooms

import androidx.core.net.toUri
import androidx.navigation.NavController
import com.xsofty.shared.nav.DeepLinkHandler
import com.xsofty.shared.nav.contracts.RoomsNavContract

class RoomsNavImpl : RoomsNavContract {

    override fun show(categoryId: String, navController: NavController) {
        navController.navigate(
            "${DeepLinkHandler.ROOMS}?categoryId=$categoryId".toUri()
        )
    }
}