package com.xsofty.shared.storage

import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

class PreferencesCleaner(
    private val sharedPreferences: SharedPreferences
) : DataCleaner.Cleanable {

    @RequiresApi(Build.VERSION_CODES.GINGERBREAD)
    override fun clean() {
        sharedPreferences.edit().clear().apply()
    }
}

@Singleton
class DataCleaner @Inject constructor(
    private val cleanables: Provider<Set<Cleanable>>
) {

    fun clean() {
        try {
            for (cleaner in cleanables.get()) {
                try {
                    cleaner.clean()
                } catch (e: Throwable) {
                    continue
                }
            }
        } catch (e: Throwable) {
            // something went wrong
        }
    }

    interface Cleanable {

        @Throws(Throwable::class)
        fun clean()
    }
}