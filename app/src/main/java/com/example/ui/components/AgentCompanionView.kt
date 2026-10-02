package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.AgendaEvent

@Composable
fun AgentCompanionView(
    allEvents: List<AgendaEvent>,
    memories: List<AgendaEvent>,
    isSpeaking: Boolean,
    onTestVoice: (String, String) -> Unit,
    onOpenMarketAnalysis: () -> Unit,
    onAskAdvice: () -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = allEvents.count { it.isCompleted }
    val pendingCount = allEvents.count { !it.isCompleted }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Avatar Card
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.agent_avatar_aura),
                    contentDescription = "Avatar de Aura",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Aura",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Tu Agente Personal de Vida & Recuerdos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(100.dp),
                        color = if (isSpeaking) Color(0xFF10B981).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (isSpeaking) "🎙️ Hablando ahora..." else "✨ Conectada y lista para recordarte",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isSpeaking) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$pendingCount", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(text = "Por cumplir", style = MaterialTheme.typography.labelSmall)
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${memories.size}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                    Text(text = "Recuerdos", style = MaterialTheme.typography.labelSmall)
                }
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.weight(1f)
            ) {
                Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "$completedCount", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                    Text(text = "Completados", style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // Voice Personalities Sandbox
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Voces del Agente",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Prueba cómo suena cada tono para tus alarmas:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                listOf(
                    "Cálida" to "¡Hola! Estoy aquí para acompañarte y recordarte tus días más bonitos.",
                    "Motivadora" to "¡A levantarse con todo el ánimo! Hoy alcanzaremos grandes metas.",
                    "Ejecutiva" to "Atención: Recordatorio puntual de compromisos y objetivos clave.",
                    "Poética" to "Los momentos más especiales se graban en el alma para siempre."
                ).forEach { (persona, sampleText) ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = persona, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                                Text(text = sampleText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(
                                onClick = { onTestVoice(sampleText, persona) },
                                modifier = Modifier.testTag("test_voice_$persona")
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = "Escuchar", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // Market Analysis Button
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lightbulb, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "¿Existe algo parecido en el mercado?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aprende cómo VoxAgenda supera a Google Calendar, TimeTree y Notion con dibujos, alarmas habladas y memoria emocional.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onOpenMarketAnalysis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_open_market_agent_tab")
                ) {
                    Icon(Icons.Default.Info, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ver Estudio de Mercado e Innovación")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
