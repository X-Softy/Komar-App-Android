package com.xsofty.shared.nav.contracts

import androidx.navigation.NavController

interface RoomDetailsNavContract {
    fun show(roomId: String, navController: NavController)
}