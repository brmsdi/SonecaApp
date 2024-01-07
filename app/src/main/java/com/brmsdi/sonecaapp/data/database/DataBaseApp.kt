package com.brmsdi.sonecaapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.brmsdi.sonecaapp.data.constant.Constant
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.DATABASE.DB_CURRENT_VERSION
import com.brmsdi.sonecaapp.model.Alarm
import com.brmsdi.sonecaapp.model.AlarmDayWeek
import com.brmsdi.sonecaapp.model.Day
import com.brmsdi.sonecaapp.model.Distance
import com.brmsdi.sonecaapp.model.TypeDistance
import com.brmsdi.sonecaapp.data.dao.AlarmWithDaysOfWeekDao
import com.brmsdi.sonecaapp.data.dao.DayDao
import com.brmsdi.sonecaapp.data.dao.DistanceDao
import com.google.android.libraries.places.api.model.DayOfWeek.*

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
@Database(
    entities = [
        Alarm::class,
        Distance::class,
        AlarmDayWeek::class,
        Day::class],
    version = DB_CURRENT_VERSION,
)
abstract class DataBaseApp : RoomDatabase() {
    abstract fun dayDao(): DayDao
    abstract fun distanceDao(): DistanceDao
    abstract fun alarmWithDaysOfWeekDao(): AlarmWithDaysOfWeekDao

    companion object {
        private lateinit var INSTANCE: DataBaseApp
        fun initialize(context: Context): DataBaseApp {
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(DataBaseApp::class) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        DataBaseApp::class.java,
                        Constant.Companion.DATABASE.DB_NAME
                    )
                        .allowMainThreadQueries()
                        .build()
                }
            }
            setup()
            return INSTANCE
        }

        private fun setup() {
            val dayOfWeekDao = INSTANCE.dayDao()
            val distanceDao = INSTANCE.distanceDao()
            if (dayOfWeekDao.count() == 0) {
                dayOfWeekDao.insert(
                    listOf(
                        Day(
                            SUNDAY.ordinal.toLong(),
                            SUNDAY.name
                        ),
                        Day(
                            MONDAY.ordinal.toLong(),
                            MONDAY.name
                        ),
                        Day(
                            TUESDAY.ordinal.toLong(),
                            TUESDAY.name
                        ),
                        Day(
                            WEDNESDAY.ordinal.toLong(),
                            WEDNESDAY.name
                        ),
                        Day(
                            THURSDAY.ordinal.toLong(),
                            THURSDAY.name
                        ),
                        Day(
                            FRIDAY.ordinal.toLong(),
                            FRIDAY.name
                        ),
                        Day(
                            SATURDAY.ordinal.toLong(),
                            SATURDAY.name
                        )
                    )
                )
            } // END IF DAY OF WEEK COUNT
            if (distanceDao.count() == 0) {
                distanceDao.insert(
                    listOf(
                        Distance(
                            value = 10,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 20,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 30,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 40,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 50,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 60,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 70,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 80,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 90,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 100,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 200,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 300,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 400,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 500,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 600,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 700,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 800,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 900,
                            typeDistance = TypeDistance.METERS.id.toLong()
                        ),
                        Distance(
                            value = 1,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 2,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 3,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 4,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 5,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 6,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 7,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 8,
                            typeDistance = TypeDistance.KM.id.toLong()
                        ),
                        Distance(
                            value = 9,
                            typeDistance = TypeDistance.KM.id.toLong()
                        )
                    )
                )
            }
        }
    }
}