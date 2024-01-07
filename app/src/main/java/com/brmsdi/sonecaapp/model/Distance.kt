package com.brmsdi.sonecaapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Distance(
    @PrimaryKey(autoGenerate = true) val distanceId: Long = 0,
    val value: Int,
    val typeDistance: Long)