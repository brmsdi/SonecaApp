package com.brmsdi.sonecaapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
@Entity(
    primaryKeys = ["alarmId", "dayId"],
    foreignKeys = [
        ForeignKey(
            entity = Alarm::class,
            parentColumns = ["alarmId"],
            childColumns = ["alarmId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Day::class,
            parentColumns = ["dayId"],
            childColumns = ["dayId"]
        )
    ],
    indices = [Index("dayId")]
)
data class AlarmDayWeek(
    val alarmId: Long,
    val dayId: Long
)