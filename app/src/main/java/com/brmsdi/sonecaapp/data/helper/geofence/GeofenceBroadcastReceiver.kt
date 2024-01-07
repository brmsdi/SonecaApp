package com.brmsdi.sonecaapp.data.helper.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.GEOFENCE.ID_ALARM
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.getCurrentDay
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.getHourAndMinutes
import com.brmsdi.sonecaapp.repository.local.AlarmWithDaysOfWeekRepository
import com.brmsdi.sonecaapp.utils.NotificationUtils.Companion.createNotificationAlarm
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent
import org.koin.java.KoinJavaComponent.inject

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
private const val TAG = "ErrorGeofenceBroadcast"

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val alarmId = intent.getLongExtra(ID_ALARM, 0L)
        if (geofencingEvent != null) {
            if (geofencingEvent.hasError()) {
                val errorMessage = GeofenceStatusCodes
                    .getStatusCodeString(geofencingEvent.errorCode)
                Log.e(TAG, errorMessage)
                return
            }
        }
        if (alarmId == 0L) {
            return
        }
        val geofenceTransition = geofencingEvent!!.geofenceTransition
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER
            || geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {
            val repo: AlarmWithDaysOfWeekRepository by inject(AlarmWithDaysOfWeekRepository::class.java)
            val alarmWithDaysOfWeek = repo.getAlarmById(alarmId)
            val alarm = alarmWithDaysOfWeek.alarm
            if (!alarm.isActivated) return
            val currentDay = getCurrentDay()
            val count = alarmWithDaysOfWeek.daysOfWeek.count { day -> day.dayId.toInt() == currentDay }
            if (count == 0) return
            val hoursAndMinutes = getHourAndMinutes()
            if (hoursAndMinutes.first < alarm.hour
                || (hoursAndMinutes.first == alarm.hour
                &&
                hoursAndMinutes.second < alarm.minutes)) {
                return
            }
            // NOTIFICATION
            createNotificationAlarm(context, alarmWithDaysOfWeek)
        }
    }
}