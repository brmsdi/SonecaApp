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
    override suspend fun insert(daysOfWeek: List<Day>) {
        return dayOfWeekDao.insert(daysOfWeek)
    }

    override suspend fun count(): Int {
        return dayOfWeekDao.count()
    }

    override suspend fun getAll(): List<Day> {
        return dayOfWeekDao.getAll()
    }
}