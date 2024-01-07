package com.brmsdi.sonecaapp.repository.local

import com.brmsdi.sonecaapp.model.Distance

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface DistanceRepository {
    fun insert(distances: List<Distance>)

    fun getAll() : List<Distance>

    fun count() : Int
}