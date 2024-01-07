package com.brmsdi.sonecaapp.utils

import android.content.Context
import android.content.res.Configuration

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class ColorUtil private constructor() {
    companion object {
        fun isDarkMode(context: Context): Boolean {
            return when (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_YES -> true
                else -> false
            }
        }
    }
}