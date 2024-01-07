package com.brmsdi.sonecaapp.di

import com.brmsdi.sonecaapp.data.database.DataBaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataBaseAppModule = module {
    single<DataBaseApp> { DataBaseApp.initialize(androidContext()) }
}