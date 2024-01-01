package com.brmsdi.sonecaapp.utils

import android.content.res.Resources
import com.brmsdi.sonecaapp.data.listeners.models.Distance
import com.brmsdi.sonecaapp.data.listeners.models.TypeDistance
import com.google.android.gms.maps.model.LatLng
import kotlin.math.ln


/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface Zoom {
    fun generate(latLng: LatLng, distance: Distance, resources: Resources) : Float {
        val earthRadius = 6371009.0
        val meters = if (distance.typeDistance == TypeDistance.KM) distance.value*1000 else distance.value
        val screenSize = resources.displayMetrics.widthPixels.coerceAtMost(resources.displayMetrics.heightPixels)
        val metersPerPixel = (meters / (2 * Math.PI * earthRadius)) * (1 shl 20)
        return (16 - ln(metersPerPixel) + ln(screenSize.toDouble() / 256.0)).toFloat()
    }
}