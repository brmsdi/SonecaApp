package com.brmsdi.sonecaapp.utils

import android.graphics.Color
import com.brmsdi.sonecaapp.data.listeners.models.Alarm
import com.brmsdi.sonecaapp.data.listeners.models.TypeDistance
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class MapUtils {
    companion object {
        fun addCircle(googleMap: GoogleMap, latLng: LatLng, alarm: Alarm): Circle {
            var radius = alarm.distance.value.toDouble()
            val distance = alarm.distance
            if (distance.typeDistance == TypeDistance.KM) radius = (distance.value * 1000.0)
            return googleMap.addCircle(
                CircleOptions()
                    .center(latLng)
                    .fillColor(Color.argb(50, 70, 130, 180))
                    .strokeColor(Color.WHITE)
                    .strokeWidth(4f)
                    .radius(radius)
                    .clickable(true)
            )
        }

        fun addInfoWindow(
            googleMap: GoogleMap,
            latLng: LatLng,
            title: String,
            snippet: String,
            visible: Boolean = true
        ): Marker? {
            val options = MarkerOptions()
                .title(title)
                .snippet(snippet)
                .position(latLng)
                .visible(visible)
            val marker = googleMap.addMarker(options)
            marker?.showInfoWindow()
            return marker
        }
    }
}