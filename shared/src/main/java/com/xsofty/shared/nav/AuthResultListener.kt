package com.xsofty.shared.nav

interface AuthResultListener {
    fun onAuthSucceeded()
    fun onAuthFailed()
    fun fireSignInIntent()
    fun signOut()
}