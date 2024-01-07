package com.brmsdi.sonecaapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
@Entity
data class Day(@PrimaryKey val dayId: Long, @Ignore val name: String) {
    constructor(dayId: Long) : this(dayId, "")
}