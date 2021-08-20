package com.xsofty.auth.helper

interface AuthResultListener {
    fun onAuthSucceeded()
    fun onAuthFailed()
}