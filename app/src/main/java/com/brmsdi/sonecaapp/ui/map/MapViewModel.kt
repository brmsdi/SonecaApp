package com.brmsdi.sonecaapp.ui.map

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.GEOFENCE.ID_ALARM
import com.brmsdi.sonecaapp.data.dto.AlarmDialogData
import com.brmsdi.sonecaapp.data.helper.geofence.GeofenceBroadcastReceiver
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.model.TypeDistance
import com.brmsdi.sonecaapp.repository.local.AlarmWithDaysOfWeekRepository
import com.brmsdi.sonecaapp.repository.local.DayOfWeekRepository
import com.brmsdi.sonecaapp.repository.local.DistanceRepository
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices

class MapViewModel(
    private val application: Application,
    private val dayOfWeekRepository: DayOfWeekRepository,
    private val distanceRepository: DistanceRepository,
    private val alarmWithDaysOfWeekRepository: AlarmWithDaysOfWeekRepository
) :
    AndroidViewModel(application) {
    private val _alarmWithDaysOfWeek = MutableLiveData<List<AlarmWithDaysOfWeek>>(listOf())
    val alarmWithDaysOfWeek: LiveData<List<AlarmWithDaysOfWeek>> = _alarmWithDaysOfWeek
    private val _alarmDialogData = MutableLiveData<AlarmDialogData>()
    val alarmDialogData: LiveData<AlarmDialogData> = _alarmDialogData
    private val _alarmWithDaysOfWeekSaved = MutableLiveData<AlarmWithDaysOfWeek>()
    val alarmWithDaysOfWeekSaved: LiveData<AlarmWithDaysOfWeek> = _alarmWithDaysOfWeekSaved
    private val geofencingClient = LocationServices.getGeofencingClient(application)
    fun newAlarm(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
        _alarmWithDaysOfWeekSaved.value =
            alarmWithDaysOfWeekRepository.prepareInsert(alarmWithDaysOfWeek)
        _alarmWithDaysOfWeekSaved.value?.let {
            addGeofencing(it)
        }
    }

    fun getDataDialog() {
        val alarmDialogData = AlarmDialogData()
        alarmDialogData.listDaysOfWeek = dayOfWeekRepository.getAll()
        alarmDialogData.listDistances = distanceRepository.getAll()
        _alarmDialogData.value = alarmDialogData
    }

    fun getAllAlarms() {
        _alarmWithDaysOfWeek.value = alarmWithDaysOfWeekRepository.getAll()
    }

    fun delete(alarmId: Long) {
        alarmWithDaysOfWeekRepository.deleteAlarmWithDaysOfWeek(alarmId)
        removeGeofence(alarmId.toString())
        getAllAlarms()
    }

    @SuppressLint("MissingPermission")
    fun addGeofencing(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
        val alarm = alarmWithDaysOfWeek.alarm
        val distance = alarmWithDaysOfWeek.distance
        val meters =
            if (distance.typeDistance.toInt() == TypeDistance.KM.id) distance.value * 10000 else distance.value
        val geofence = Geofence.Builder()
            .setRequestId(alarm.alarmId.toString())
            .setCircularRegion(
                alarm.latitude,
                alarm.longitude,
                meters.toFloat()
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setLoiteringDelay(5000)
            .build()
        geofencingClient.addGeofences(
            getGeofencingRequest(geofence),
            geofencePendingIntent(alarmWithDaysOfWeek)
        )
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(geofence)
        }.build()
    }

    private fun geofencePendingIntent(alarmWithDaysOfWeek: AlarmWithDaysOfWeek): PendingIntent {
        val intent = Intent(application, GeofenceBroadcastReceiver::class.java)
        intent.putExtra(ID_ALARM, alarmWithDaysOfWeek.alarm.alarmId)
        return PendingIntent.getBroadcast(
            application,
            alarmWithDaysOfWeek.alarm.alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun removeGeofence(geofenceId: String) {
        geofencingClient.removeGeofences(listOf(geofenceId))
    }
}