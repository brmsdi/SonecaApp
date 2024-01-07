package com.brmsdi.sonecaapp.data.dto

import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.Marker

data class CircleWithMarkerDTO(
    val alarmId: Long,
    val circle: Circle,
    val marker: Marker
)
