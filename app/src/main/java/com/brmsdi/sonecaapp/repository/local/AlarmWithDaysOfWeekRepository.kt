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

    fun getAll() : List<AlarmWithDaysOfWeek>
    fun getAlarmWithDaysOfWeekById(id: Long): AlarmWithDaysOfWeek?

    fun insertAlarm(alarm: Alarm) : Long

    fun insertAlarmDayWeek(alarmDayWeek: List<AlarmDayWeek>)

    fun prepareInsert(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) : AlarmWithDaysOfWeek?

    fun deleteAlarmWithDaysOfWeek(alarmId: Long)

    fun getAlarmById(alarmId: Long) : AlarmWithDaysOfWeek
}