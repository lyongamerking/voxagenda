package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.AgendaEvent

@Composable
fun EventVisualThumbnail(
    event: AgendaEvent,
    modifier: Modifier = Modifier,
    enableZoomOnClick: Boolean = true
) {
    val context = LocalContext.current
    val shape = RoundedCornerShape(12.dp)
    var showFullViewDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .clip(shape)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .then(
                if (enableZoomOnClick) Modifier.clickable { showFullViewDialog = true }
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        when (event.visualType) {
            "PHOTO" -> {
                if (event.photoUri.isNotBlank()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(Uri.parse(event.photoUri))
                            .crossfade(true)
                            .build(),
                        contentDescription = "Foto del evento",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.65f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.PhotoCamera,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
            "AI_ART" -> {
                val resId = when {
                    event.aiArtStyle.contains("scenic", ignoreCase = true) ||
                    event.category.contains("Viaje", ignoreCase = true) -> R.drawable.ai_art_scenic
                    else -> R.drawable.ai_art_celebration
                }
                Image(
                    painter = painterResource(id = resId),
                    contentDescription = "Arte IA del evento",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                Surface(
                    shape = CircleShape,
                    color = Color(0xFF6366F1).copy(alpha = 0.85f),
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(4.dp)
                        .size(16.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
            else -> {
                // DOODLE
                DoodleThumbnail(
                    drawing = event.getDoodle(),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    // High-Resolution Dialog Viewer on Click
    if (showFullViewDialog && enableZoomOnClick) {
        Dialog(onDismissRequest = { showFullViewDialog = false }) {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = event.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = when (event.visualType) {
                                    "PHOTO" -> "📷 Fotografía del evento"
                                    "AI_ART" -> "✨ Ilustración artística por IA"
                                    else -> "🎨 Dibujo original hecho a mano"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { showFullViewDialog = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .clip(RoundedCornerShape(18.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(18.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        when (event.visualType) {
                            "PHOTO" -> {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(Uri.parse(event.photoUri))
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = "Foto completa",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            "AI_ART" -> {
                                val resId = when {
                                    event.aiArtStyle.contains("scenic", ignoreCase = true) ||
                                    event.category.contains("Viaje", ignoreCase = true) -> R.drawable.ai_art_scenic
                                    else -> R.drawable.ai_art_celebration
                                }
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = "Arte IA",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            else -> {
                                DoodleThumbnail(
                                    drawing = event.getDoodle(),
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                        }
                    }

                    if (event.visualType == "AI_ART") {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Estilo: ${event.aiArtStyle.ifBlank { "Celebración & Acuarela" }}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
