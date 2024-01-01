package com.brmsdi.sonecaapp.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.CompoundButton.OnCheckedChangeListener
import com.brmsdi.sonecaapp.data.listeners.DialogConfirmAndCancelListener
import com.brmsdi.sonecaapp.data.listeners.dtos.SpinnerDTO
import com.brmsdi.sonecaapp.data.listeners.models.Alarm
import com.brmsdi.sonecaapp.data.listeners.models.Distance
import com.brmsdi.sonecaapp.data.listeners.models.DistanceSpinner
import com.brmsdi.sonecaapp.data.listeners.models.TypeDistance
import com.brmsdi.sonecaapp.databinding.AddNewAlarmBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.DayOfWeek

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class DialogUtil private constructor() {
    companion object {
        private fun listDistance(): List<DistanceSpinner> {
            return mutableListOf(
                DistanceSpinner(Distance(10, TypeDistance.METROS)),
                DistanceSpinner(Distance(20, TypeDistance.METROS)),
                DistanceSpinner(Distance(30, TypeDistance.METROS)),
                DistanceSpinner(Distance(40, TypeDistance.METROS)),
                DistanceSpinner(Distance(50, TypeDistance.METROS)),
                DistanceSpinner(Distance(60, TypeDistance.METROS)),
                DistanceSpinner(Distance(70, TypeDistance.METROS)),
                DistanceSpinner(Distance(80, TypeDistance.METROS)),
                DistanceSpinner(Distance(90, TypeDistance.METROS)),
                DistanceSpinner(Distance(100, TypeDistance.METROS)),
                DistanceSpinner(Distance(200, TypeDistance.METROS)),
                DistanceSpinner(Distance(300, TypeDistance.METROS)),
                DistanceSpinner(Distance(400, TypeDistance.METROS)),
                DistanceSpinner(Distance(500, TypeDistance.METROS)),
                DistanceSpinner(Distance(600, TypeDistance.METROS)),
                DistanceSpinner(Distance(700, TypeDistance.METROS)),
                DistanceSpinner(Distance(800, TypeDistance.METROS)),
                DistanceSpinner(Distance(900, TypeDistance.METROS)),
                DistanceSpinner(Distance(1, TypeDistance.KM)),
                DistanceSpinner(Distance(2, TypeDistance.KM)),
                DistanceSpinner(Distance(3, TypeDistance.KM)),
                DistanceSpinner(Distance(4, TypeDistance.KM)),
                DistanceSpinner(Distance(5, TypeDistance.KM)),
                DistanceSpinner(Distance(6, TypeDistance.KM)),
                DistanceSpinner(Distance(7, TypeDistance.KM)),
                DistanceSpinner(Distance(8, TypeDistance.KM)),
                DistanceSpinner(Distance(9, TypeDistance.KM)),
            )
        }

        fun createDialog(
            context: Context,
            latLng: LatLng,
            listener: DialogConfirmAndCancelListener,
            onCheckedChangeListener: OnCheckedChangeListener
        ): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context)
            val layoutInflater = LayoutInflater.from(context)
            val dialogBinding = AddNewAlarmBinding.inflate(layoutInflater)
            val spinnerDistance = dialogBinding.spinnerDistance
            val spinnerDistanceAdapter: ArrayAdapter<SpinnerDTO<Distance>> = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                listDistance()
            )
            spinnerDistance.adapter = spinnerDistanceAdapter
            dialogBinding.dayOfWeekSunday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.dayOfWeekMonday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.dayOfWeekTuesday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.dayOfWeekWednesday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.dayOfWeekThursday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.dayOfWeekFriday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.dayOfWeekSaturday.setOnCheckedChangeListener(onCheckedChangeListener)
            dialogBinding.buttonCancel.setOnClickListener { listener.cancel() }
            dialogBinding.buttonConfirm.setOnClickListener {
                val selectedDistance = spinnerDistance.selectedItem as DistanceSpinner
                val days = mutableListOf<Int>()
                if (dialogBinding.dayOfWeekSunday.isChecked) days.add(DayOfWeek.SUNDAY.ordinal)
                if (dialogBinding.dayOfWeekMonday.isChecked) days.add(DayOfWeek.MONDAY.ordinal)
                if (dialogBinding.dayOfWeekTuesday.isChecked) days.add(DayOfWeek.TUESDAY.ordinal)
                if (dialogBinding.dayOfWeekWednesday.isChecked) days.add(DayOfWeek.WEDNESDAY.ordinal)
                if (dialogBinding.dayOfWeekThursday.isChecked) days.add(DayOfWeek.THURSDAY.ordinal)
                if (dialogBinding.dayOfWeekFriday.isChecked) days.add(DayOfWeek.FRIDAY.ordinal)
                if (dialogBinding.dayOfWeekSaturday.isChecked) days.add(DayOfWeek.SATURDAY.ordinal)
                val alarm = Alarm(
                    id = null,
                    title = dialogBinding.editDescription.text.toString(),
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    distance = selectedDistance.getModel(),
                    daysOfWeek = days,
                    hour = dialogBinding.timeInitial.hour,
                    minutes = dialogBinding.timeInitial.minute
                )
                listener.confirm(alarm)
            }
            val timeInitial = dialogBinding.timeInitial
            timeInitial.setIs24HourView(true)
            dialogBuilder.setView(dialogBinding.root)
            return dialogBuilder.create()
        }

        fun closeDialog(alertDialog: AlertDialog) {
            if (alertDialog.isShowing) alertDialog.dismiss()
        }
    }
}