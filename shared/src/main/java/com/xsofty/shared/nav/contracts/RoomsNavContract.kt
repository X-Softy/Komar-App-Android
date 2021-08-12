package com.xsofty.shared.nav.contracts

import androidx.navigation.NavController

interface RoomsNavContract {
    fun show(categoryId: String, navController: NavController)
}