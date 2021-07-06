package com.xsofty.shared.storage

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class PrefDelegate<T>(val prefKey: String) :
    ReadWriteProperty<PrefDelegate.PreferenceProvider, T> {

    interface PreferenceProvider {
        val sharedPreferences: SharedPreferences
    }
}

fun stringPref(prefKey: String, defaultValue: String? = null) =
    StringPrefDelegate(prefKey, defaultValue)

class StringPrefDelegate(prefKey: String, private val defaultValue: String?) :
    PrefDelegate<String?>(prefKey) {

    override fun getValue(thisRef: PreferenceProvider, property: KProperty<*>): String? =
        thisRef.sharedPreferences.getString(prefKey, defaultValue)

    override fun setValue(thisRef: PreferenceProvider, property: KProperty<*>, value: String?) =
        thisRef.sharedPreferences.edit { putString(prefKey, value) }
}