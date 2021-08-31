package com.xsofty.shared.storage

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

interface AppPreferences {
    var idToken: String?
}

@Singleton
class AppPreferencesImpl @Inject constructor(
    override val sharedPreferences: SharedPreferences
) : AppPreferences, PrefDelegate.PreferenceProvider {

    override var idToken: String? by stringPref(PREF_ID_TOKEN)

    companion object {
        private const val PREF_ID_TOKEN = "id_token"
    }
}
