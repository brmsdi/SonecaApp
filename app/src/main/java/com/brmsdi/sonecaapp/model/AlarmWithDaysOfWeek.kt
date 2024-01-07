package com.brmsdi.sonecaapp.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
data class AlarmWithDaysOfWeek(
    @Embedded val alarm: Alarm,
    @Relation (
        parentColumn = "alarmDistanceId",
        entityColumn = "distanceId"
    )
    val distance: Distance,
    @Relation(
        parentColumn = "alarmId",
        entityColumn = "dayId",
        associateBy = Junction(AlarmDayWeek::class)
    )
    val daysOfWeek: List<Day>

)