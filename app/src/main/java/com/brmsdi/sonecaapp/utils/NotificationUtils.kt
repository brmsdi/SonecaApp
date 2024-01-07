package com.brmsdi.sonecaapp.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.brmsdi.sonecaapp.R
import com.brmsdi.sonecaapp.data.constant.Constant
import com.brmsdi.sonecaapp.data.helper.geofence.AlarmNotificationActionReceiver
import com.brmsdi.sonecaapp.model.AlarmWithDaysOfWeek

/**
 *
 * @author Wisley Bruno Marques França
 * @since 1
 */
class NotificationUtils private constructor() {

    companion object {
        fun createNotificationAlarm(context: Context, alarmWithDaysOfWeek: AlarmWithDaysOfWeek) {
            val alarm = alarmWithDaysOfWeek.alarm
            val vibrator = context.getSystemService(Vibrator::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    Constant.Companion.NOTIFICATION.CHANNEL_ID,
                    Constant.Companion.NOTIFICATION.NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.description = Constant.Companion.NOTIFICATION.ALARM_DESCRIPTION
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
                notificationManager?.createNotificationChannel(channel)
                // VIBRATOR
                val vibrationPattern = longArrayOf(500, 1000)
                val vibrationEffect = VibrationEffect.createWaveform(vibrationPattern, 0)
                vibrator?.vibrate(vibrationEffect)
            } else vibrator?.vibrate(longArrayOf(500, 1000), 0)

            val builder = NotificationCompat.Builder(
                context,
                Constant.Companion.NOTIFICATION.CHANNEL_ID
            )
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(alarm.title)
                .setContentText(context.getString(R.string.alarm_content_text))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(createNotificationDownAction(context, alarm.alarmId.toInt()))
            // Show the notification
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            NotificationManagerCompat.from(context).notify(alarm.alarmId.toInt(), builder.build())
            notificationCancel(vibrator)
        }

        private fun createNotificationDownAction(context: Context, notificationId: Int): NotificationCompat.Action {
            val intent = Intent(context, AlarmNotificationActionReceiver::class.java)
            intent.putExtra(Constant.Companion.NOTIFICATION.ID, notificationId)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            return NotificationCompat.Action.Builder(
                R.drawable.ic_launcher_foreground,
                context.getString(R.string.down),
                pendingIntent
            ).build()
        }

        private fun notificationCancel(vibrator: Vibrator?) {
            Handler(Looper.getMainLooper())
                .postDelayed({
                             vibrator?.let {
                                 if (it.hasVibrator()) {
                                     it.cancel()
                                 }
                             }
                }, 20 * 1000)
        }
    }
}