package com.brmsdi.sonecaapp.repository.local

import com.brmsdi.sonecaapp.model.Day

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface DayOfWeekRepository {
    suspend fun insert(daysOfWeek: List<Day>)

    suspend fun count() : Int

    suspend fun getAll() : List<Day>
}