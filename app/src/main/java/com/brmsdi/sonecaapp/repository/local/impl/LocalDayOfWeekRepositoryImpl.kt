package com.brmsdi.sonecaapp.repository.local.impl

import com.brmsdi.sonecaapp.model.Day
import com.brmsdi.sonecaapp.data.database.DataBaseApp
import com.brmsdi.sonecaapp.repository.local.DayOfWeekRepository

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class LocalDayOfWeekRepositoryImpl(dataBaseApp: DataBaseApp) : DayOfWeekRepository {
    private val dayOfWeekDao = dataBaseApp.dayDao()
    override fun insert(daysOfWeek: List<Day>) {
        return dayOfWeekDao.insert(daysOfWeek)
    }

    override fun count(): Int {
        return dayOfWeekDao.count()
    }

    override fun getAll(): List<Day> {
        return dayOfWeekDao.getAll()
    }
}