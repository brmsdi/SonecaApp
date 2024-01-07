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
    suspend fun getAll() : List<AlarmWithDaysOfWeek>

    @Transaction
    @Query("SELECT * FROM Alarm WHERE alarmId = :id")
    suspend fun getAlarmWithDaysOfWeekById(id: Long): AlarmWithDaysOfWeek?

    @Insert
    suspend fun insertAlarm(alarm: Alarm) : Long

    @Insert
    suspend fun insertAlarmDayWeek(alarmDayWeek: List<AlarmDayWeek>)

    @Transaction
    suspend fun prepareInsert(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) : AlarmWithDaysOfWeek? {
        val alarmID = insertAlarm(alarmWithDaysOfWeek.alarm)
        insertAlarmDayWeek(alarmWithDaysOfWeek.daysOfWeek.map { AlarmDayWeek(alarmID, it.dayId) })
        return getAlarmWithDaysOfWeekById(alarmID)
    }

    @Transaction
    @Query("DELETE FROM Alarm WHERE alarmId = :alarmId")
    suspend fun deleteAlarmWithDaysOfWeek(alarmId: Long)
}