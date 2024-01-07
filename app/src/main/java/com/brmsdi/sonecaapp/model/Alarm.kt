package com.brmsdi.sonecaapp.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Distance::class,
            parentColumns = ["distanceId"],
            childColumns = ["alarmDistanceId"]
        )
    ],
    indices = [Index("alarmDistanceId")]
)
data class Alarm(
    @PrimaryKey(autoGenerate = true) val alarmId: Long = 0,
    val title: String,
    val latitude: Double,
    val longitude: Double,
    val alarmDistanceId: Long,
    val hour: Int,
    val minutes: Int,
    val isActivated: Boolean = true
)