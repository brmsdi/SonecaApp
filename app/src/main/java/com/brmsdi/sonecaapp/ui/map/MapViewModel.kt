package com.brmsdi.sonecaapp.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brmsdi.sonecaapp.data.dto.AlarmDialogData
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.repository.local.AlarmWithDaysOfWeekRepository
import com.brmsdi.sonecaapp.repository.local.DayOfWeekRepository
import com.brmsdi.sonecaapp.repository.local.DistanceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MapViewModel(
    application: Application,
    private val dayOfWeekRepository: DayOfWeekRepository,
    private val distanceRepository: DistanceRepository,
    private val alarmWithDaysOfWeekRepository: AlarmWithDaysOfWeekRepository
) :
    AndroidViewModel(application) {
    private val _alarmWithDaysOfWeek = MutableLiveData<List<AlarmWithDaysOfWeek>>(listOf())
    val alarmWithDaysOfWeek: LiveData<List<AlarmWithDaysOfWeek>> = _alarmWithDaysOfWeek
    private val _alarmDialogData = MutableLiveData<AlarmDialogData>()
    val alarmDialogData : LiveData<AlarmDialogData> = _alarmDialogData
    private val _alarmWithDaysOfWeekSaved = MutableLiveData<AlarmWithDaysOfWeek>()
    val alarmWithDaysOfWeekSaved : LiveData<AlarmWithDaysOfWeek> = _alarmWithDaysOfWeekSaved

    fun newAlarm(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
        viewModelScope.launch {
            _alarmWithDaysOfWeekSaved.value = alarmWithDaysOfWeekRepository.prepareInsert(alarmWithDaysOfWeek)
        }
    }

    fun getDataDialog() {
        viewModelScope.launch {
            val job1 = async { dayOfWeekRepository.getAll() }
            val job2 = async { distanceRepository.getAll() }
            val alarmDialogData = AlarmDialogData()
            alarmDialogData.listDaysOfWeek = job1.await()
            alarmDialogData.listDistances = job2.await()
            _alarmDialogData.value  = alarmDialogData
        }
    }

    fun getAllAlarms() {
        viewModelScope.launch {
            _alarmWithDaysOfWeek.value = alarmWithDaysOfWeekRepository.getAll()
        }
    }

    fun delete(alarmId: Long) {
        viewModelScope.launch {
            alarmWithDaysOfWeekRepository.deleteAlarmWithDaysOfWeek(alarmId)
            getAllAlarms()
        }
    }
}