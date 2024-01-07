package com.brmsdi.sonecaapp.di

import com.brmsdi.sonecaapp.repository.local.AlarmWithDaysOfWeekRepository
import com.brmsdi.sonecaapp.repository.local.DayOfWeekRepository
import com.brmsdi.sonecaapp.repository.local.DistanceRepository
import com.brmsdi.sonecaapp.repository.local.impl.LocalAlarmWithDaysOfWeekRepositoryImpl
import com.brmsdi.sonecaapp.repository.local.impl.LocalDayOfWeekRepositoryImpl
import com.brmsdi.sonecaapp.repository.local.impl.LocalDistanceRepositoryImpl
import org.koin.dsl.module

val RepositoryModules = module {
    single<DayOfWeekRepository> { LocalDayOfWeekRepositoryImpl(get()) }
    single<DistanceRepository> { LocalDistanceRepositoryImpl(get()) }
    single<AlarmWithDaysOfWeekRepository> { LocalAlarmWithDaysOfWeekRepositoryImpl(get()) }
}