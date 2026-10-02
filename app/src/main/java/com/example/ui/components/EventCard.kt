package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AgendaEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EventCard(
    event: AgendaEvent,
    onSpeakVoice: () -> Unit,
    onTriggerTestAlarm: () -> Unit,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSendGift: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("d 'de' MMM, yyyy • h:mm a", Locale("es", "ES")) }
    val formattedDateTime = remember(event.dateTimeEpochMs) {
        dateFormatter.format(Date(event.dateTimeEpochMs))
    }

    val doodle = remember(event.doodleJson) { event.getDoodle() }
    val hasDoodle = doodle.strokes.isNotEmpty() || doodle.stamps.isNotEmpty()

    val importanceColor = when (event.importance) {
        "Prioritaria" -> MaterialTheme.colorScheme.error
        "Alta" -> Color(0xFFF59E0B)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("event_card_${event.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (event.isCompleted) {
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (event.isCompleted) 1.dp else 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row: Category, Importance, Memory badge, Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Category Chip
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = event.category,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Importance Indicator
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = importanceColor.copy(alpha = 0.15f),
                    modifier = Modifier.padding(end = 6.dp)
                ) {
                    Text(
                        text = event.importance,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = importanceColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Memory capsule badge
                if (event.isMemoryCapsule) {
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, Modifier.size(12.dp), tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Recuerdo",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp).testTag("btn_edit_${event.id}")) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp).testTag("btn_delete_${event.id}")) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Content: Checkbox, Title & Time, and miniature Doodle Thumbnail
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Complete toggle checkbox
                IconButton(
                    onClick = onToggleCompleted,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(top = 2.dp)
                        .testTag("toggle_complete_${event.id}")
                ) {
                    Icon(
                        imageVector = if (event.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "Completar",
                        tint = if (event.isCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.outline
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if (event.isCompleted) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (event.isCompleted) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = formattedDateTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    if (event.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = event.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            maxLines = 2
                        )
                    }

                    if (event.reflectionNote.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "💌 Nota: \"${event.reflectionNote}\"",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.secondary,
                            maxLines = 2
                        )
                    }
                }

                // Visual Thumbnail (Doodle, Photo from Gallery, or AI Art)
                val hasVisual = (event.visualType == "PHOTO" && event.photoUri.isNotBlank()) ||
                                (event.visualType == "AI_ART") ||
                                (event.visualType == "DOODLE" && hasDoodle)
                if (hasVisual) {
                    Spacer(modifier = Modifier.width(10.dp))
                    EventVisualThumbnail(
                        event = event,
                        modifier = Modifier.size(68.dp, 56.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Voice info banner
            if (event.isAlarmEnabled) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Voz: ${event.voicePersona} • Alarma programada",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Affiliate Gift Suggestion Banner for Special Dates
            val isGiftCategory = event.category in listOf("Cumpleaños", "Aniversario", "Especial", "Familia", "Recuerdo")
            if (isGiftCategory && onSendGift != null) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFFBEB),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSendGift() }
                        .padding(bottom = 10.dp)
                        .testTag("gift_banner_${event.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "💐", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Enviar flores o regalo (Cupón AURA10)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "Sugerencia con entrega a domicilio programada",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFB45309)
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Bottom Buttons: Speak preview & Trigger Live Alarm Now
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onSpeakVoice,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_speak_${event.id}")
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = null, Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Escuchar Voz", style = MaterialTheme.typography.labelMedium)
                }

                FilledTonalButton(
                    onClick = onTriggerTestAlarm,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_trigger_alarm_${event.id}")
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Probar Alarma", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
