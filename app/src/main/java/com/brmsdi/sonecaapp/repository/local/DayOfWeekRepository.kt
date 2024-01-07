package com.brmsdi.sonecaapp.repository.local

import com.brmsdi.sonecaapp.model.Day

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface DayOfWeekRepository {
    fun insert(daysOfWeek: List<Day>)

    fun count() : Int

    fun getAll() : List<Day>
}