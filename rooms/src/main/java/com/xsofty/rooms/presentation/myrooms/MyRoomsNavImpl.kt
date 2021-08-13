package com.xsofty.rooms.presentation.myrooms

import androidx.core.net.toUri
import androidx.navigation.NavController
import com.xsofty.shared.nav.DeepLinkHandler
import com.xsofty.shared.nav.contracts.MyRoomsNavContract

class MyRoomsNavImpl : MyRoomsNavContract {

    override fun show(navController: NavController) {
        navController.navigate(
            DeepLinkHandler.MY_ROOMS.toUri()
        )
    }
}