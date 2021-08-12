package com.xsofty.rooms.presentation.create

import androidx.core.net.toUri
import androidx.navigation.NavController
import com.xsofty.shared.nav.DeepLinkHandler
import com.xsofty.shared.nav.contracts.CreateRoomNavContract

class CreateRoomNavImpl : CreateRoomNavContract {

    override fun show(navController: NavController) {
        navController.navigate(
            DeepLinkHandler.CREATE_ROOM.toUri()
        )
    }
}