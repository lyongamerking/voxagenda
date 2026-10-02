package com.example.voice

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.data.AgendaEvent
import com.example.receiver.AlarmReceiver

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun scheduleAlarm(event: AgendaEvent) {
        if (!event.isAlarmEnabled) return
        val targetTime = event.dateTimeEpochMs
        if (targetTime <= System.currentTimeMillis()) return

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_EVENT_ID, event.id)
            putExtra(AlarmReceiver.EXTRA_EVENT_TITLE, event.title)
            putExtra(AlarmReceiver.EXTRA_EVENT_MESSAGE, event.getEffectiveVoiceMessage())
            putExtra(AlarmReceiver.EXTRA_EVENT_PERSONA, event.voicePersona)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            event.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager?.canScheduleExactAlarms() == true) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        targetTime,
                        pendingIntent
                    )
                } else {
                    alarmManager?.set(
                        AlarmManager.RTC_WAKEUP,
                        targetTime,
                        pendingIntent
                    )
                }
            } else {
                alarmManager?.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    targetTime,
                    pendingIntent
                )
            }
        } catch (_: SecurityException) {
            alarmManager?.set(
                AlarmManager.RTC_WAKEUP,
                targetTime,
                pendingIntent
            )
        }
    }

    fun scheduleTestAlarm(event: AgendaEvent, delaySeconds: Int = 3) {
        val targetTime = System.currentTimeMillis() + (delaySeconds * 1000L)
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(AlarmReceiver.EXTRA_EVENT_ID, event.id)
            putExtra(AlarmReceiver.EXTRA_EVENT_TITLE, "🔔 " + event.title)
            putExtra(AlarmReceiver.EXTRA_EVENT_MESSAGE, event.getEffectiveVoiceMessage())
            putExtra(AlarmReceiver.EXTRA_EVENT_PERSONA, event.voicePersona)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            (event.id + 9999).toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.set(
            AlarmManager.RTC_WAKEUP,
            targetTime,
            pendingIntent
        )
    }

    fun cancelAlarm(eventId: Long) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            eventId.toInt(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager?.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }
}
