package com.brmsdi.sonecaapp.ui.alarm.adapter

import android.content.Context
import android.location.Location
import android.view.View.GONE
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.brmsdi.sonecaapp.R
import com.brmsdi.sonecaapp.data.listener.LocationCalcListener
import com.brmsdi.sonecaapp.databinding.CardAlarmsBinding
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.model.TypeDistance
import com.google.android.gms.maps.model.LatLng

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class AlarmsViewHolder(
    val context: Context,
    private val row: CardAlarmsBinding,
    private val locationCalcListener: LocationCalcListener,
    private val lastLocation: Location?
) :
    ViewHolder(row.root) {
    fun bind(alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
        val daysOfWeekArray = context.resources.getStringArray(R.array.days_of_week_names)
        val textDays = mutableListOf<String>()
        val alarmsRecycler = row.cardAlarms
        val alarm = alarmWithDaysOfWeek.alarm
        val daysOfWeek = alarmWithDaysOfWeek.daysOfWeek
        val distance = alarmWithDaysOfWeek.distance
        var currentDistance : Float? = null
        row.textTitle.text = alarm.title
        val hour = if (alarm.hour.toString().length == 1) "0".plus(alarm.hour.toString()) else alarm.hour.toString()
        val minutes = if (alarm.minutes.toString().length == 1) "0".plus(alarm.minutes.toString()) else alarm.minutes.toString()
        row.textTime.text = context.getString(R.string.initial_time)
            .plus(String.format(" %s:%s", hour, minutes))
        row.textDaysLabel.text =
            if (daysOfWeek.size > 1) context.getString(R.string.days_of_week_alarm) else context.getString(
                R.string.day_of_week_alarm
            )
        daysOfWeek.forEach {
            for ((index, day) in daysOfWeekArray.withIndex()) {
                if (it.dayId.toInt() == index) {
                    textDays.add(day.toString().substring(0, 3))
                    break
                }
            }
        }
        row.textDays.text = String.format("%s", textDays.joinToString(", "))
        if (alarm.isActivated) {
            row.iconStatus.background =
                ContextCompat.getDrawable(context, R.drawable.baseline_alarm_on_24)
            row.textStatusLabel.text = context.getString(R.string.activated)
        }
        if (lastLocation != null) {
            currentDistance =
                locationCalcListener.calculateDistance(lastLocation, LatLng(alarm.latitude, alarm.longitude))
        }
        if (currentDistance != null) {
            currentDistance = (currentDistance-distance.value)
            if (currentDistance <= 0) {
                row.textCurrentDistance.text = context.getString(R.string.you_arrived)
            } else if (currentDistance < 1000.0) {
                row.textCurrentDistance.text =
                    String.format(
                        "%s %s",
                        currentDistance.toString().substring(0, 3),
                        TypeDistance.METERS.type[0].uppercase()
                    )
            } else if (currentDistance >= 1000.0) {
                val km = (currentDistance/1000)
                var kmString = km.toString()
                kmString = if (kmString.length <= 5) kmString else kmString.substring(0, 5)
                row.textCurrentDistance.text =
                    String.format(
                        "%s %s",
                        kmString,
                        TypeDistance.KM.type.substring(0, 2).uppercase()
                    )
            }
        } else {
            row.textCurrentDistance.visibility = GONE
            row.textDistanceLabel.visibility = GONE
        }

        val backgroundDrawable = when (AlarmsListAdapter.drawable) {
            0 -> ContextCompat.getDrawable(context, R.drawable.background_card_alarm_0)
            1 -> ContextCompat.getDrawable(context, R.drawable.background_card_alarm_1)
            2 -> ContextCompat.getDrawable(context, R.drawable.background_card_alarm_2)
            3 -> ContextCompat.getDrawable(context, R.drawable.background_card_alarm_3)
            else -> ContextCompat.getDrawable(context, R.drawable.background_card_alarm_4)
        }
        alarmsRecycler.background = backgroundDrawable
        AlarmsListAdapter.drawable++
        AlarmsListAdapter.drawable =
            if (AlarmsListAdapter.drawable > 4) 0 else AlarmsListAdapter.drawable
    }
}