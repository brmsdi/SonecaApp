package com.brmsdi.sonecaapp.data.listener

import android.location.Location
import com.google.android.gms.maps.model.LatLng

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface LocationCalcListener {
    fun calculateDistance(location: Location, alarmLocation: LatLng) : Float
}