package com.brmsdi.sonecaapp.di

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val ServiceModules = module {
    single<FusedLocationProviderClient> { LocationServices.getFusedLocationProviderClient(androidContext()) }
}