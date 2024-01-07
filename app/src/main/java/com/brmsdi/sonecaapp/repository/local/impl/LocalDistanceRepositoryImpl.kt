package com.brmsdi.sonecaapp.repository.local.impl

import com.brmsdi.sonecaapp.model.Distance
import com.brmsdi.sonecaapp.data.database.DataBaseApp
import com.brmsdi.sonecaapp.repository.local.DistanceRepository

class LocalDistanceRepositoryImpl(dataBaseApp: DataBaseApp) : DistanceRepository {
    private val distanceDao = dataBaseApp.distanceDao()
    override fun insert(distances: List<Distance>) {
        distanceDao.insert(distances)
    }

    override fun getAll(): List<Distance> {
        return distanceDao.getAll()
    }

    override fun count(): Int {
        return distanceDao.count()
    }
}