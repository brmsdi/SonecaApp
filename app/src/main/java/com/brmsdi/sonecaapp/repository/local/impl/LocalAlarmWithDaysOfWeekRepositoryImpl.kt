package com.brmsdi.sonecaapp.repository.local.impl

import com.brmsdi.sonecaapp.model.Alarm
import com.brmsdi.sonecaapp.model.AlarmDayWeek
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.repository.local.AlarmWithDaysOfWeekRepository
import com.brmsdi.sonecaapp.data.database.DataBaseApp

class LocalAlarmWithDaysOfWeekRepositoryImpl(dataBaseApp: DataBaseApp) :
    AlarmWithDaysOfWeekRepository {
    private val alarmWithDaysOfWeekDao = dataBaseApp.alarmWithDaysOfWeekDao()
    override suspend fun getAll(): List<AlarmWithDaysOfWeek> {
        return alarmWithDaysOfWeekDao.getAll()
    }

    override suspend fun getAlarmWithDaysOfWeekById(id: Long): AlarmWithDaysOfWeek? {
        return alarmWithDaysOfWeekDao.getAlarmWithDaysOfWeekById(id)
    }

    override suspend fun insertAlarm(alarm: Alarm): Long {
        return alarmWithDaysOfWeekDao.insertAlarm(alarm)
    }

    override suspend fun insertAlarmDayWeek(alarmDayWeek: List<AlarmDayWeek>) {
        alarmWithDaysOfWeekDao.insertAlarmDayWeek(alarmDayWeek)
    }

    override suspend fun prepareInsert(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) : AlarmWithDaysOfWeek? {
        return alarmWithDaysOfWeekDao.prepareInsert(alarmWithDaysOfWeek)
    }

    override suspend fun deleteAlarmWithDaysOfWeek(alarmId: Long) {
        alarmWithDaysOfWeekDao.deleteAlarmWithDaysOfWeek(alarmId)
    }
}