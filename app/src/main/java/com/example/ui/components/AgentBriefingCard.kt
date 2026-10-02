package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AgendaEvent
import java.util.Calendar

@Composable
fun AgentBriefingCard(
    todayEvents: List<AgendaEvent>,
    upcomingEvents: List<AgendaEvent>,
    isSpeaking: Boolean,
    currentSpokenText: String,
    onPlayBriefing: () -> Unit,
    onStopSpeaking: () -> Unit,
    onOpenMarketAnalysis: () -> Unit,
    onQuickPrompt: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val greeting = when (hour) {
        in 5..11 -> "¡Buenos días!"
        in 12..18 -> "¡Buenas tardes!"
        else -> "¡Buenas noches!"
    }

    val infiniteTransition = rememberInfiniteTransition(label = "avatar_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("agent_briefing_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
                            Color.Transparent
                        ),
                        radius = 800f
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Agent Avatar with speaking animation
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(68.dp)
                    ) {
                        if (isSpeaking) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.agent_avatar_aura),
                            contentDescription = "Avatar de Aura",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Aura • Agente Personal",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (isSpeaking) Color(0xFF10B981) else Color(0xFF6366F1),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                        Text(
                            text = greeting,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (todayEvents.isNotEmpty()) {
                                "Tienes ${todayEvents.size} evento(s) para hoy"
                            } else {
                                "Tu día está tranquilo y despejado"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Button to open market analysis & comparison
                    IconButton(
                        onClick = onOpenMarketAnalysis,
                        modifier = Modifier.testTag("btn_market_info")
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = "Estudio de Mercado e Innovación",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Spoken text indicator when speaking
                AnimatedVisibility(visible = isSpeaking && currentSpokenText.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = currentSpokenText,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                maxLines = 3
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action buttons: Daily Voice Briefing
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (isSpeaking) {
                        Button(
                            onClick = onStopSpeaking,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_stop_speaking")
                        ) {
                            Icon(Icons.Default.StopCircle, contentDescription = null, Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Silenciar Voz")
                        }
                    } else {
                        Button(
                            onClick = onPlayBriefing,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_play_briefing")
                        ) {
                            Icon(Icons.Default.RecordVoiceOver, contentDescription = null, Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("🎙️ Resumen por Voz")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Interactive Agent dialogue chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(
                        listOf(
                            "¿Qué tengo hoy?",
                            "¿Cuál es mi próximo evento?",
                            "¿Cápsula de recuerdos?",
                            "Dame un consejo del día"
                        )
                    ) { prompt ->
                        Surface(
                            shape = RoundedCornerShape(100.dp),
                            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.clickable { onQuickPrompt(prompt) }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    modifier = Modifier.size(12.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = prompt,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
