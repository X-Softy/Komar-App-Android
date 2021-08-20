package com.xsofty.komarista.auth

interface AuthResultListener {
    fun onAuthSucceeded()
    fun onAuthFailed()
}