package com.brmsdi.sonecaapp.repository.local

import com.brmsdi.sonecaapp.model.Distance

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface DistanceRepository {
    suspend fun insert(distances: List<Distance>)

    suspend fun getAll() : List<Distance>

    suspend fun count() : Int
}