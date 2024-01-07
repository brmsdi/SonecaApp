package com.brmsdi.sonecaapp.repository.local

import com.brmsdi.sonecaapp.model.Alarm
import com.brmsdi.sonecaapp.model.AlarmDayWeek
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface AlarmWithDaysOfWeekRepository {

    suspend fun getAll() : List<AlarmWithDaysOfWeek>
    suspend fun getAlarmWithDaysOfWeekById(id: Long): AlarmWithDaysOfWeek?

    suspend fun insertAlarm(alarm: Alarm) : Long

    suspend fun insertAlarmDayWeek(alarmDayWeek: List<AlarmDayWeek>)

    suspend fun prepareInsert(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) : AlarmWithDaysOfWeek?

    suspend fun deleteAlarmWithDaysOfWeek(alarmId: Long)
}