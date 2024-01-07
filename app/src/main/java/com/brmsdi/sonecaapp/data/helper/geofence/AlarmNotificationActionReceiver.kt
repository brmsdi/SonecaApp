package com.brmsdi.sonecaapp.data.helper.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import androidx.core.app.NotificationManagerCompat
import com.brmsdi.sonecaapp.data.constant.Constant

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class AlarmNotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val vibrator = context.getSystemService(
            Vibrator::class.java
        )
        vibrator?.cancel()
        val notificationId = intent.getIntExtra(Constant.Companion.NOTIFICATION.ID, 0)
        if (notificationId != 0) NotificationManagerCompat.from(context).cancel(notificationId)
    }
}