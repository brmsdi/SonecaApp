package com.brmsdi.sonecaapp.ui.map

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brmsdi.sonecaapp.data.listeners.models.Alarm

class MapViewModel(application: Application) : AndroidViewModel(application) {
    private val _alarms = MutableLiveData<List<Alarm>>()
    val alarms : LiveData<List<Alarm>> = _alarms

    fun newAlarm(alarms: MutableLiveData<List<Alarm>>) {
        _alarms.value = alarms.value
    }
}