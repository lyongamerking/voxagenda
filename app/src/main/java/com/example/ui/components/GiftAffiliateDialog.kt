package com.example.ui.components

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.AgendaEvent

data class GiftItem(
    val title: String,
    val category: String,
    val priceEstimate: String,
    val iconEmoji: String,
    val description: String,
    val partnerStore: String,
    val commissionPercent: String,
    val simulatedUrl: String
)

val SampleGiftCatalog = listOf(
    GiftItem(
        title = "Ramo de Rosas & Lilis Primaverales",
        category = "Flores Frescas",
        priceEstimate = "$38.00 USD",
        iconEmoji = "💐",
        description = "Arreglo floral fresco entregado a domicilio en caja de lujo con tarjeta personalizada.",
        partnerStore = "EnvíaFlores / Interflora Partner",
        commissionPercent = "12% de comisión",
        simulatedUrl = "https://www.enviaflores.com?ref=voxagenda"
    ),
    GiftItem(
        title = "Desayuno Sorpresa & Globos Festivos",
        category = "Experiencias",
        priceEstimate = "$45.00 USD",
        iconEmoji = "🥐",
        description = "Canasta con croissant recién horneado, café de especialidad, jugo y globos temáticos.",
        partnerStore = "Gourmet Express Partner",
        commissionPercent = "10% de comisión",
        simulatedUrl = "https://www.gourmetexpress.com?ref=voxagenda"
    ),
    GiftItem(
        title = "Cofre de Chocolates Belgas Artesanales",
        category = "Dulces & Delicias",
        priceEstimate = "$26.00 USD",
        iconEmoji = "🍫",
        description = "Selección de 24 trufas artesanales con licor y frutos secos para endulzar el día.",
        partnerStore = "Chocolaterie Partner",
        commissionPercent = "15% de comisión",
        simulatedUrl = "https://www.chocolates.com?ref=voxagenda"
    ),
    GiftItem(
        title = "Dije de Plata Grabado con Fecha Especial",
        category = "Joyería Emocional",
        priceEstimate = "$52.00 USD",
        iconEmoji = "💍",
        description = "Colgante de plata esterlina con la fecha del evento grabada en alta precisión.",
        partnerStore = "Joyas & Memorias Partner",
        commissionPercent = "14% de comisión",
        simulatedUrl = "https://www.joyas.com?ref=voxagenda"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GiftAffiliateDialog(
    targetEvent: AgendaEvent?,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var copiedCoupon by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🎁 Regalos y Flores con Descuento",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("gifts_close_btn")) {
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
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Event Context banner
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = "💝", style = MaterialTheme.typography.titleMedium)
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sugerencias de Aura para:",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = targetEvent?.title ?: "Tu persona especial",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Envía flores o regalos con entrega a domicilio programada.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Coupon code card (Tracking Affiliate mechanism)
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFBEB)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCD34D))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFD97706))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Cupón Exclusivo de Afiliado:",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFD97706),
                                modifier = Modifier.clickable { copiedCoupon = true }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "AURA10",
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        Icons.Default.ContentCopy,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Aplica el código AURA10 para un 10% de descuento al pagar. La tienda rastrea automáticamente tu compra y acredita tu comisión.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF78350F)
                        )
                    }
                }

                // Gifts catalog
                Text(
                    text = "Catálogo de Socios Oficiales:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )

                SampleGiftCatalog.forEach { gift ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    modifier = Modifier.size(46.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(text = gift.iconEmoji, style = MaterialTheme.typography.titleLarge)
                                    }
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = gift.title,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${gift.category} • ${gift.partnerStore}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Text(
                                    text = gift.priceEstimate,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = gift.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "💰 Ganancia de la App: ${gift.commissionPercent}",
                                        color = Color(0xFF047857),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Button(
                                    onClick = {
                                        try {
                                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(gift.simulatedUrl))
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            // Fallback
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Icon(Icons.Default.ShoppingBag, contentDescription = null, Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Ver Tienda", style = MaterialTheme.typography.labelMedium)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}
