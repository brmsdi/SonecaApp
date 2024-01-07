package com.brmsdi.sonecaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.TABLE.DISTANCE_NAME
import com.brmsdi.sonecaapp.model.Distance

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
@Dao
interface DistanceDao {
    @Insert
    fun insert(distances: List<Distance>)

    @Query("SELECT * FROM $DISTANCE_NAME")
    fun getAll() : List<Distance>

    @Query("SELECT COUNT(*) FROM $DISTANCE_NAME")
    fun count() : Int
}