package com.xsofty.shared.theme

import android.content.Context
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.xsofty.shared.R

enum class ColorType(val lightId: Int, val darkId: Int) {
    PRIMARY(R.color.primary_light, R.color.primary_dark),
    SECONDARY(R.color.secondary_light, R.color.secondary_dark),
    QUATERNARY(R.color.quaternary_light, R.color.quaternary_dark),
    TERTIARY(R.color.tertiary_light, R.color.tertiary_dark),
    TEXT_PRIMARY(R.color.on_primary_light, R.color.on_primary_dark),
    TEXT_SECONDARY(R.color.on_secondary_light, R.color.on_secondary_dark),
    BACKGROUND(R.color.background_light, R.color.background_dark),
    BACKGROUND_SECONDARY(R.color.background_secondary_light, R.color.background_secondary_dark),
    INPUT(R.color.input_light, R.color.input_dark),
    NATIVE_BUTTON_DISABLED(R.color.native_button_enabled, R.color.native_button_disabled),
    HEADER(R.color.header_light, R.color.header_dark)
}


class ThemeManager(private val context: Context) {

    var backgroundImageId: Int = 0
        private set
        get() =
            if (isDarkThemeOn(context)) R.drawable.background_dark
            else R.drawable.background_light

    var primaryColorId: Int = 0
        private set
        get() =
            if (isDarkThemeOn(context)) R.color.primary_dark
            else R.color.primary_light

    @Composable
    fun getColor(colorType: ColorType): Color {
        return colorResource(
            if (isDarkThemeOn(context)) colorType.darkId
            else colorType.lightId
        )
    }

    private fun isDarkThemeOn(context: Context): Boolean {
        return context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
    }
}