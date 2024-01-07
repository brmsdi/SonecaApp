package com.brmsdi.sonecaapp.data.constant

import android.content.Context
import com.brmsdi.sonecaapp.R
import com.brmsdi.sonecaapp.model.Day

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class Constant private constructor() {
    companion object {
        object DATABASE {
            const val DB_NAME = "sonecadb"
            const val DB_CURRENT_VERSION = 1
        }
        object TABLE {
            const val DAY_WEEK_NAME = "Day"
            const val DISTANCE_NAME = "Distance"
            const val ALARM_NAME = "Alarm"
        }

        fun getConstDays(context: Context) : MutableList<Day> {
            val days = context.resources.getStringArray(R.array.days_of_week_names)
            val listDays = mutableListOf<Day>()
            for (i in 0..6) {
                listDays.add(Day(i.toLong(), days[i]))
            }
            return listDays
        }
    }
}