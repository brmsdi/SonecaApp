package com.brmsdi.sonecaapp.utils

import android.graphics.Color
import android.location.Location
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.model.TypeDistance
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
        fun addCircle(googleMap: GoogleMap, latLng: LatLng, alarmWithDaysOfWeek: AlarmWithDaysOfWeek): Circle {
            val distance = alarmWithDaysOfWeek.distance
            var radius = distance.value.toDouble()
            val typeDistance = TypeDistance.getForID(distance.typeDistance)
            if (typeDistance == TypeDistance.KM) radius = (distance.value * 1000.0)
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
            visible: Boolean = true
        ): Marker? {
            val options = MarkerOptions()
                .title(title)
                .position(latLng)
                .visible(visible)
            val marker = googleMap.addMarker(options)
            marker?.showInfoWindow()
            return marker
        }

        fun addMyLocationCircle(googleMap: GoogleMap, location: Location) : Circle {
            return googleMap.addCircle(
                CircleOptions()
                    .center(LatLng(location.latitude, location.longitude))
                    .fillColor(Color.argb(255, 70, 130, 180))
                    .strokeColor(Color.WHITE)
                    .strokeWidth(2f)
                    .radius(4.0)
                    .zIndex(5.0f)
            )
        }

        fun getRatio(initialValue: Float = .4f, currentZoom: Float, maxZoomLevel: Float): Float {
            var initial = initialValue
            var index = 21
            for (value in 21 downTo (1)) {
                val decrementedIndex = index - 1
                if (currentZoom <= value && currentZoom >= decrementedIndex) {
                    return (initial + maxZoomLevel)
                }
                index--
                initial = (initial * 2)
            }
            return maxZoomLevel + 1294.334f
        }
    }
}