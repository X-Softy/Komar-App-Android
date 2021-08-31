package com.xsofty.komarista.helper

interface AuthResultListener {
    fun onAuthSucceeded()
    fun onAuthFailed()
    fun fireSignInIntent()
}