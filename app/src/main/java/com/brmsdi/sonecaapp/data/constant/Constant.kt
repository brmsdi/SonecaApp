package com.brmsdi.sonecaapp.data.constant

import android.content.Context
import com.brmsdi.sonecaapp.R
import com.brmsdi.sonecaapp.model.Day
import java.util.Calendar

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

        object GEOFENCE {
            const val ID_ALARM = "ID_ALARM"
        }

        object NOTIFICATION {
            const val CHANNEL_ID = "12"
            const val NAME = "ALARM NOTIFICATION"
            const val ALARM_DESCRIPTION = "ALARMS NOTIFICATIONS CHANNEL"
            const val ID = "NOTIFICATION_ID"
        }

        fun getConstDays(context: Context) : MutableList<Day> {
            val days = context.resources.getStringArray(R.array.days_of_week_names)
            val listDays = mutableListOf<Day>()
            for (i in 0..6) {
                listDays.add(Day(i.toLong(), days[i]))
            }
            return listDays
        }

        fun getCurrentDay(): Int {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_WEEK)-1
        }

        fun getHourAndMinutes(): Pair<Int, Int> {
            val calendar = Calendar.getInstance()
            return Pair(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))
        }
    }
}