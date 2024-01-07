package com.brmsdi.sonecaapp.ui.alarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.repository.local.AlarmWithDaysOfWeekRepository
import kotlinx.coroutines.launch

class AlarmViewModel(
    application: Application,
    private val alarmWithDaysOfWeekRepository: AlarmWithDaysOfWeekRepository
) : AndroidViewModel(application) {
    private val _data = MutableLiveData<List<AlarmWithDaysOfWeek>>()
    val data : LiveData<List<AlarmWithDaysOfWeek>> = _data

    fun updateData() {
        viewModelScope.launch {
            _data.value = alarmWithDaysOfWeekRepository.getAll()
        }
    }
}