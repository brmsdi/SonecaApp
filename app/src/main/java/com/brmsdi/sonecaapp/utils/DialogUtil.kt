package com.brmsdi.sonecaapp.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.CompoundButton.OnCheckedChangeListener
import android.widget.Toast
import android.widget.ToggleButton
import com.brmsdi.sonecaapp.R
import com.brmsdi.sonecaapp.data.constant.Constant.Companion.getConstDays
import com.brmsdi.sonecaapp.data.dto.AlarmDialogData
import com.brmsdi.sonecaapp.data.listener.DialogConfirmAndCancelListener
import com.brmsdi.sonecaapp.data.dto.SpinnerDTO
import com.brmsdi.sonecaapp.model.Alarm
import com.brmsdi.sonecaapp.model.Distance
import com.brmsdi.sonecaapp.data.dto.DistanceSpinnerDTO
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek
import com.brmsdi.sonecaapp.model.Day
import com.brmsdi.sonecaapp.databinding.AddNewAlarmBinding
import com.google.android.gms.maps.model.LatLng

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class DialogUtil private constructor() {
    companion object {

        data class ToggleData(val toggleButton: ToggleButton, val day: Long)

        private fun listDistance(distances: List<Distance>): List<DistanceSpinnerDTO> {
            return distances.map { DistanceSpinnerDTO(it) }
        }

        private fun getDays(context: Context, toggleButtons: List<ToggleData>): MutableList<Day> {
            val days = getConstDays(context)
            val result = mutableListOf<Day>()
            toggleButtons.forEach { toggleData ->
                val toggleButton = toggleData.toggleButton
                val day = toggleData.day
                if (toggleButton.isChecked) {
                    for (dayOfWeek in days) {
                        if (day == dayOfWeek.dayId) {
                            result.add(
                                Day(
                                    dayOfWeek.dayId,
                                    dayOfWeek.name
                                )
                            )
                            break
                        } // END-IF
                    } //END FOR DAYS
                } // END IF TOGGLE BUTTON CHECK
            } // END FOR TOGGLE BUTTONS
            return result
        }

        fun createDialog(
            context: Context,
            latLng: LatLng,
            listener: DialogConfirmAndCancelListener,
            onCheckedChangeListener: OnCheckedChangeListener,
            alarmDialogData: AlarmDialogData
        ): AlertDialog {
            val dialogBuilder = AlertDialog.Builder(context)
            val layoutInflater = LayoutInflater.from(context)
            val dialogBinding = AddNewAlarmBinding.inflate(layoutInflater)
            val spinnerDistance = dialogBinding.spinnerDistance
            val spinnerDistanceAdapter: ArrayAdapter<SpinnerDTO<Distance>> = ArrayAdapter(
                context,
                android.R.layout.simple_spinner_dropdown_item,
                listDistance(alarmDialogData.listDistances)
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
                if (dialogBinding.editDescription.text.toString().isEmpty()) {
                    dialogBinding.editDescription.requestFocus()
                    Toast.makeText(context, R.string.msg_error_text_empty, Toast.LENGTH_LONG)
                        .show()
                    return@setOnClickListener
                }

                val selectedDistance = spinnerDistance.selectedItem as DistanceSpinnerDTO
                val distance = selectedDistance.getModel()
                val alarm = Alarm(
                    title = dialogBinding.editDescription.text.toString(),
                    latitude = latLng.latitude,
                    longitude = latLng.longitude,
                    alarmDistanceId = distance.distanceId,
                    hour = dialogBinding.timeInitial.hour,
                    minutes = dialogBinding.timeInitial.minute
                )
                val days = mutableListOf<Day>()
                val buttons = listOf(
                    ToggleData(
                        dialogBinding.dayOfWeekSunday, 0L
                    ),
                    ToggleData(
                        dialogBinding.dayOfWeekMonday, 1L
                    ),
                    ToggleData(
                        dialogBinding.dayOfWeekTuesday, 2L
                    ),
                    ToggleData(
                        dialogBinding.dayOfWeekWednesday, 3L
                    ),
                    ToggleData(
                        dialogBinding.dayOfWeekThursday, 4L
                    ),
                    ToggleData(
                        dialogBinding.dayOfWeekFriday, 5L
                    ),
                    ToggleData(
                        dialogBinding.dayOfWeekSaturday, 6L
                    )
                )
                days.addAll(getDays(context, buttons))
                if (days.isEmpty()) {
                    Toast.makeText(
                        context,
                        R.string.msg_error_days_not_selected,
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }
                listener.confirm(AlarmWithDaysOfWeek(alarm, distance, days))
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