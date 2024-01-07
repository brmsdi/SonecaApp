package com.brmsdi.sonecaapp.data.listener

import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface DialogConfirmAndCancelListener {
    fun confirm(alarmWithDaysOfWeek: AlarmWithDaysOfWeek)
    fun cancel()
}