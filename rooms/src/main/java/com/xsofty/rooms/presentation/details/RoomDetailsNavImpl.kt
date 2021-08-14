package com.xsofty.rooms.presentation.details

import androidx.core.net.toUri
import androidx.navigation.NavController
import com.xsofty.shared.nav.DeepLinkHandler
import com.xsofty.shared.nav.contracts.RoomDetailsNavContract

class RoomDetailsNavImpl : RoomDetailsNavContract {

    override fun show(roomId: String, navController: NavController) {
        navController.navigate(
            "${DeepLinkHandler.ROOM_DETAILS}?roomId=$roomId".toUri()
        )
    }
}