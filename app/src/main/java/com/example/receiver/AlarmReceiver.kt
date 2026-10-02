package com.example.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

data class TriggeredAlarmData(
    val eventId: Long,
    val title: String,
    val voiceMessage: String,
    val persona: String
)

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
        const val EXTRA_EVENT_TITLE = "extra_event_title"
        const val EXTRA_EVENT_MESSAGE = "extra_event_message"
        const val EXTRA_EVENT_PERSONA = "extra_event_persona"

        const val CHANNEL_ID = "vox_alarm_channel"

        private val _alarmEvents = MutableSharedFlow<TriggeredAlarmData>(extraBufferCapacity = 10)
        val alarmEvents: SharedFlow<TriggeredAlarmData> = _alarmEvents.asSharedFlow()
    }

    override fun onReceive(context: Context, intent: Intent) {
        val eventId = intent.getLongExtra(EXTRA_EVENT_ID, -1L)
        val title = intent.getStringExtra(EXTRA_EVENT_TITLE) ?: "Recordatorio del Agente"
        val message = intent.getStringExtra(EXTRA_EVENT_MESSAGE) ?: "¡Tienes un evento importante ahora!"
        val persona = intent.getStringExtra(EXTRA_EVENT_PERSONA) ?: "Cálida"

        // Broadcast to in-app listeners if UI is alive
        _alarmEvents.tryEmit(TriggeredAlarmData(eventId, title, message, persona))

        // Create System Notification
        showNotification(context, eventId, title, message)
    }

    private fun showNotification(context: Context, eventId: Long, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarmas de Voz del Agente",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de eventos y recuerdos con mensaje de voz del agente"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val launchIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(EXTRA_EVENT_ID, eventId)
            putExtra(EXTRA_EVENT_TITLE, title)
            putExtra(EXTRA_EVENT_MESSAGE, message)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            eventId.toInt(),
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("🎙️ $title")
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(eventId.toInt(), notification)
    }
}
