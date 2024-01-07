package com.brmsdi.sonecaapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.TABLE.DAY_WEEK_NAME
import com.brmsdi.sonecaapp.model.Day

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
@Dao
interface DayDao {
    @Insert
    fun insert(daysOfWeek: List<Day>)
    @Query("SELECT COUNT(*) FROM $DAY_WEEK_NAME")
    fun count() : Int

    @Query("SELECT * FROM $DAY_WEEK_NAME")
    fun getAll() : List<Day>
}