package com.brmsdi.sonecaapp.data.listeners

import com.brmsdi.sonecaapp.data.listeners.models.Alarm


/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */

interface DialogConfirmAndCancelListener {
    fun confirm(alarm: Alarm)
    fun cancel()
}