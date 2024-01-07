package com.brmsdi.sonecaapp.di

import com.brmsdi.sonecaapp.ui.alarm.AlarmViewModel
import com.brmsdi.sonecaapp.ui.map.MapViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelModules = module {
    viewModel { MapViewModel(get(), get(), get(), get()) }
    viewModel { AlarmViewModel(get(), get()) }
}