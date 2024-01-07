package com.brmsdi.sonecaapp.model

import androidx.room.Embedded
import androidx.room.Relation


/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
data class AlarmWithDistance(
    @Embedded val alarm: Alarm,
    @Relation (
        parentColumn = "alarmDistanceID",
        entityColumn = "distanceId"
    )
    val distance: Distance
)