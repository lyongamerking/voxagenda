package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.AgendaEvent

@Composable
fun VoiceAlarmDialog(
    event: AgendaEvent,
    isSpeaking: Boolean,
    onSpeakAgain: () -> Unit,
    onSnooze: () -> Unit,
    onDismiss: () -> Unit
) {
    // Pulse animation for avatar ring & soundwave
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .testTag("voice_alarm_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header tag
                Surface(
                    shape = RoundedCornerShape(100.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.GraphicEq,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ALARMA DE VOZ ACTIVA",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Avatar with glowing ring
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(110.dp)
                ) {
                    if (isSpeaking) {
                        Box(
                            modifier = Modifier
                                .size(105.dp)
                                .scale(pulseScale)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.agent_avatar_aura),
                        contentDescription = "Avatar de Aura",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(86.dp)
                            .clip(CircleShape)
                            .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = event.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "Categoría: ${event.category} • Voz: ${event.voicePersona}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Visual preview (Doodle, Photo, or AI Art)
                val doodle = event.getDoodle()
                val hasVisual = (event.visualType == "PHOTO" && event.photoUri.isNotBlank()) ||
                                (event.visualType == "AI_ART") ||
                                (event.visualType == "DOODLE" && (doodle.strokes.isNotEmpty() || doodle.stamps.isNotEmpty()))
                if (hasVisual) {
                    Text(
                        text = when (event.visualType) {
                            "PHOTO" -> "Foto del evento:"
                            "AI_ART" -> "Arte ilustrado del evento:"
                            else -> "Tu dibujo del evento:"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    EventVisualThumbnail(
                        event = event,
                        modifier = Modifier.size(130.dp, 84.dp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Speech card bubble
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🗣️ Mensaje del Agente:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (isSpeaking) {
                                Text(
                                    text = "Hablando...",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "\"${event.getEffectiveVoiceMessage()}\"",
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = onSpeakAgain,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("alarm_btn_replay")
                    ) {
                        Icon(Icons.Default.Replay, contentDescription = null, Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Repetir")
                    }

                    FilledTonalButton(
                        onClick = onSnooze,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("alarm_btn_snooze")
                    ) {
                        Icon(Icons.Default.Snooze, contentDescription = null, Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Posponer")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("alarm_btn_dismiss")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("¡Enterado! Apagar Alarma")
                }
            }
        }
    }
}
