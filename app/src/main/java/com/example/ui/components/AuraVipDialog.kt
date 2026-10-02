package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuraVipDialog(
    isVipActive: Boolean,
    onToggleVip: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isAnnualSelected by remember { mutableStateOf(true) }
    var showSuccessMessage by remember { mutableStateOf(false) }

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
                                Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = Color(0xFFFFB300),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Aura VIP Club",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("vip_close_btn")) {
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Banner with gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6366F1),
                                    Color(0xFF8B5CF6),
                                    Color(0xFFEC4899)
                                )
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = Color(0xFFFFD54F),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Desbloquea el poder total de Aura",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Preserva los momentos más valiosos de tu vida con tecnología emocional exclusiva.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.9f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // VIP Benefits list
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Beneficios Exclusivos de la Suscripción:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        VipFeatureRow(
                            icon = Icons.Default.RecordVoiceOver,
                            iconColor = Color(0xFFE11D48),
                            title = "🗣️ Clonación de Voz Emocional",
                            subtitle = "Graba la voz real de tu mamá, tu pareja o tus hijos para que sus voces te despierten y feliciten."
                        )

                        VipFeatureRow(
                            icon = Icons.Default.AutoAwesome,
                            iconColor = Color(0xFF6366F1),
                            title = "✨ Arte & Bocetos por IA Ilimitados",
                            subtitle = "Convierte cada fecha en una acuarela, boceto o pintura con estilos artísticos premium sin límites."
                        )

                        VipFeatureRow(
                            icon = Icons.Default.CloudDone,
                            iconColor = Color(0xFF059669),
                            title = "☁️ Respaldo Infinito en la Nube",
                            subtitle = "Tus fotos, audios y dibujos estarán a salvo para siempre aunque cambies de móvil."
                        )

                        VipFeatureRow(
                            icon = Icons.Default.Star,
                            iconColor = Color(0xFFD97706),
                            title = "🏷️ Descuentos VIP en Flores y Libros",
                            subtitle = "15% de descuento permanente en envíos de flores y en el libro físico de recuerdos."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Pricing toggle (Annual vs Monthly)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isAnnualSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { isAnnualSelected = true }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "ANUAL (Ahorra 37%)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = if (isAnnualSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$29.99 / año ($2.49/mes)",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isAnnualSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (!isAnnualSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable { isAnnualSelected = false }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "MENSUAL",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (!isAnnualSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$3.99 / mes",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (!isAnnualSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action button: Activate / Manage VIP
                if (isVipActive) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "¡Membresía VIP Activa!",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                                Text(
                                    text = "Tienes acceso completo a todas las funciones premium.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF047857)
                                )
                            }
                            Button(
                                onClick = { onToggleVip(false) },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                            ) {
                                Text("Pausar")
                            }
                        }
                    }
                } else {
                    // PayPal Direct Button
                    Button(
                        onClick = {
                            val amount = if (isAnnualSelected) "29.99" else "3.99"
                            val itemName = if (isAnnualSelected) "Aura VIP Anual (VoxAgenda)" else "Aura VIP Mensual (VoxAgenda)"
                            val paypalUrl = "https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=acuarioeditorial@gmail.com&item_name=${Uri.encode(itemName)}&amount=$amount&currency_code=USD"
                            try {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paypalUrl))
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                // Fallback
                            }
                            onToggleVip(true)
                            showSuccessMessage = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("paypal_vip_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0070BA)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = Color(0xFFFFD54F))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAnnualSelected) "Pagar con PayPal ($29.99/año)" else "Pagar con PayPal ($3.99/mes)",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Instant Activate / Simulation Button
                    Button(
                        onClick = {
                            onToggleVip(true)
                            showSuccessMessage = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("subscribe_vip_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Activar Demostración VIP Gratuita",
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                AnimatedVisibility(visible = showSuccessMessage) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF059669).copy(alpha = 0.1f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Text(
                            text = "🎉 ¡Felicidades! Se ha activado la simulación de suscripción Aura VIP. Todas las funciones prémium están desbloqueadas.",
                            color = Color(0xFF065F46),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(12.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = "🔒 Pago 100% seguro a través de Google Play Subscriptions. Cancela en cualquier momento con un solo toque desde tus ajustes.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun VipFeatureRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = iconColor.copy(alpha = 0.12f),
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
