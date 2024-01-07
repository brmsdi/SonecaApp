package com.brmsdi.sonecaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.TABLE.ALARM_NAME
import com.brmsdi.sonecaapp.model.Alarm
import com.brmsdi.sonecaapp.model.AlarmDayWeek
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

@Dao
interface AlarmWithDaysOfWeekDao {

    @Transaction
    @Query("SELECT * FROM $ALARM_NAME")
    fun getAll() : List<AlarmWithDaysOfWeek>

    @Transaction
    @Query("SELECT * FROM Alarm WHERE alarmId = :id")
    fun getAlarmWithDaysOfWeekById(id: Long): AlarmWithDaysOfWeek?

    @Insert
    fun insertAlarm(alarm: Alarm) : Long

    @Insert
    fun insertAlarmDayWeek(alarmDayWeek: List<AlarmDayWeek>)

    @Transaction
    fun prepareInsert(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) : AlarmWithDaysOfWeek? {
        val alarmID = insertAlarm(alarmWithDaysOfWeek.alarm)
        insertAlarmDayWeek(alarmWithDaysOfWeek.daysOfWeek.map { AlarmDayWeek(alarmID, it.dayId) })
        return getAlarmWithDaysOfWeekById(alarmID)
    }

    @Transaction
    @Query("DELETE FROM Alarm WHERE alarmId = :alarmId")
    fun deleteAlarmWithDaysOfWeek(alarmId: Long)

    @Query("SELECT * FROM Alarm WHERE alarmId = :alarmId")
    fun getAlarmById(alarmId: Long) : AlarmWithDaysOfWeek
}