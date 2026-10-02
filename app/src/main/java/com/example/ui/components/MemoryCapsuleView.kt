package com.example.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AgendaEvent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MemoryCapsuleView(
    memories: List<AgendaEvent>,
    onSpeakMemory: (AgendaEvent) -> Unit,
    onAddNewMemory: () -> Unit,
    onOpenMemoryBook: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember { SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "ES")) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("memory_capsule_view")
    ) {
        // Banner
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Cápsula de Recuerdos",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tu agente personal preserva tus vivencias más hermosas, dibujos y reflexiones para que nunca los olvides.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Print-On-Demand Memory Book Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF6366F1).copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenMemoryBook() }
                .testTag("btn_open_memory_book_banner")
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF6366F1).copy(alpha = 0.15f),
                    modifier = Modifier.size(46.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color(0xFF6366F1),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📖 Imprimir Libro Físico",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4338CA)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF10B981)
                        ) {
                            Text(
                                text = "NUEVO",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                    }
                    Text(
                        text = "Convierte tus fotos, notas y dibujos en un libro de pasta dura.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF6366F1),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (memories.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "✨ Aún no tienes recuerdos guardados",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Al crear un evento, activa 'Guardar en Cápsula de Recuerdos' o añade uno ahora mismo.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAddNewMemory, modifier = Modifier.testTag("btn_add_first_memory")) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Crear Primer Recuerdo")
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(memories) { memory ->
                    val doodle = remember(memory.doodleJson) { memory.getDoodle() }
                    val hasDoodle = doodle.strokes.isNotEmpty() || doodle.stamps.isNotEmpty()

                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("memory_card_${memory.id}")
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(100.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer
                                ) {
                                    Text(
                                        text = memory.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = dateFormatter.format(Date(memory.dateTimeEpochMs)),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = memory.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            if (memory.description.isNotBlank()) {
                                Text(
                                    text = memory.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }

                            if (memory.reflectionNote.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "💌 Nota del corazón: \"${memory.reflectionNote}\"",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }

                            // Visual preview (Doodle, Photo, or AI Art)
                            val hasVisual = (memory.visualType == "PHOTO" && memory.photoUri.isNotBlank()) ||
                                            (memory.visualType == "AI_ART") ||
                                            (memory.visualType == "DOODLE" && hasDoodle)
                            if (hasVisual) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = when (memory.visualType) {
                                        "PHOTO" -> "Foto guardada con el recuerdo:"
                                        "AI_ART" -> "Arte ilustrado con el recuerdo:"
                                        else -> "Boceto guardado con el recuerdo:"
                                    },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                EventVisualThumbnail(
                                    event = memory,
                                    modifier = Modifier.size(140.dp, 80.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Action: Narrate memory
                            FilledTonalButton(
                                onClick = { onSpeakMemory(memory) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_narrate_memory_${memory.id}")
                            ) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🎙️ Escuchar Narración del Agente")
                            }
                        }
                    }
                }
            }
        }
    }
}
