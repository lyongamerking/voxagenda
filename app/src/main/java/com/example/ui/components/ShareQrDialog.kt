package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.SubcomposeAsyncImage
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShareQrDialog(
    appUrl: String = "https://ais-pre-thjsmflrrydx6vex2rla2r-493362986173.us-west1.run.app",
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var copiedMessage by remember { mutableStateOf(false) }

    val encodedUrl = remember(appUrl) {
        try {
            URLEncoder.encode(appUrl, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            appUrl
        }
    }
    // High-resolution public QR code generation endpoint
    val qrApiUrl = "https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=$encodedUrl"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.QrCode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Compartir con Código QR",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("qr_close_btn")) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "¡Muestra este código para que lo escaneen!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Cualquier persona puede apuntar con la cámara de su teléfono y abrirá VoxAgenda al instante sin instalar nada previo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                // QR Display Card with White Background for optimal scanning
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .size(260.dp)
                        .testTag("qr_code_card")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        SubcomposeAsyncImage(
                            model = qrApiUrl,
                            contentDescription = "Código QR de la app",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Fit,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            },
                            error = {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        Icons.Default.QrCode,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(72.dp)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "VoxAgenda Link",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Black
                                    )
                                }
                            }
                        )
                    }
                }

                // Link Preview Box
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = appUrl,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Actions: Copy Link & Share via Intent
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("VoxAgenda Link", appUrl)
                            clipboard.setPrimaryClip(clip)
                            copiedMessage = true
                            Toast.makeText(context, "¡Enlace copiado al portapapeles!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_copy_qr_link")
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = null, Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (copiedMessage) "¡Copiado!" else "Copiar Link")
                    }

                    Button(
                        onClick = {
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "¡Mira esta app que creé! VoxAgenda por Lyon Studio: Tu agente personal interactivo con alarmas por voz, dibujos y recuerdos familiares. Ábrela aquí: $appUrl"
                                )
                                type = "text/plain"
                            }
                            val shareIntent = Intent.createChooser(sendIntent, "Compartir VoxAgenda por Lyon Studio")
                            context.startActivity(shareIntent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("btn_share_whatsapp")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Compartir")
                    }
                }

                // Programa Invita y Gana
                var invitedFriendsCount by remember { mutableStateOf(1) }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF5FF)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFC084FC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🎁", style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Programa: ¡Invita y Gana!",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6B21A8)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF7E22CE)
                            ) {
                                Text(
                                    text = "$invitedFriendsCount / 3 Amigos",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Text(
                            text = "¿Qué ganas tú y tu amigo al unirse?",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF581C87)
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "• 👤 Tu amigo recibe: 3 Días de Aura VIP Gratis al escanear tu código.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4A044E)
                            )
                            Text(
                                text = "• 🎨 Si invitas a 1 amigo: Desbloqueas 5 Bocetos IA artísticos extras.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF4A044E)
                            )
                            Text(
                                text = "• 👑 Si invitas a 3 amigos: ¡1 MES COMPLETO de Aura VIP GRATIS!",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7E22CE)
                            )
                        }

                        // Progress Bar
                        androidx.compose.material3.LinearProgressIndicator(
                            progress = { (invitedFriendsCount / 3f).coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFF7E22CE),
                            trackColor = Color(0xFFE9D5FF)
                        )

                        if (invitedFriendsCount >= 3) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF10B981).copy(alpha = 0.2f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🎉", style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "¡Felicidades! Meta alcanzada. Reclama tu Mes VIP Gratis.",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46)
                                    )
                                }
                            }
                        } else {
                            OutlinedButton(
                                onClick = {
                                    invitedFriendsCount = (invitedFriendsCount + 1).coerceAtMost(3)
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Simular amigo escaneando QR (+1)")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Helpful Tip
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBFDBFE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "💡 Consejo para compartir y ganar:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3A8A),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Puedes imprimir este código QR en una hoja o tarjeta de presentación. Cuando la gente lo escanee, entrará a tu app y cualquier compra de regalos o libros generará tu comisión.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF1E40AF)
                        )
                    }
                }
            }
        }
    }
}
