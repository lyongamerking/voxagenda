package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

enum class BookFormatOption(
    val title: String,
    val subtitle: String,
    val priceText: String,
    val printCost: String,
    val netProfit: String,
    val iconEmoji: String,
    val isPhysical: Boolean
) {
    DIGITAL(
        title = "Digital (PDF / ePub HD)",
        subtitle = "Descarga inmediata para imprimir por tu cuenta o compartir en familia.",
        priceText = "$5.99 USD",
        printCost = "$0.00 USD (Cero costo)",
        netProfit = "+$5.99 USD (100% ganancia)",
        iconEmoji = "📱",
        isPhysical = false
    ),
    SOFTCOVER(
        title = "Tapa Blanda Económica",
        subtitle = "Encuadernación flexible, papel fotográfico premium, envío a domicilio.",
        priceText = "$18.99 USD",
        printCost = "$6.50 USD (Imprenta)",
        netProfit = "+$12.49 USD de ganancia",
        iconEmoji = "📘",
        isPhysical = true
    ),
    HARDCOVER(
        title = "Pasta Dura Deluxe",
        subtitle = "Acabado de lujo en relieve, laminado mate y estuche protector.",
        priceText = "$28.99 USD",
        printCost = "$10.50 USD (Imprenta)",
        netProfit = "+$18.49 USD de ganancia",
        iconEmoji = "📕",
        isPhysical = true
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryBookDialog(
    memories: List<AgendaEvent>,
    onDismiss: () -> Unit
) {
    var selectedFormat by remember { mutableStateOf(BookFormatOption.SOFTCOVER) }
    var recipientName by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var shippingAddress by remember { mutableStateOf("") }
    var orderSubmitted by remember { mutableStateOf(false) }

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
                                Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Color(0xFF6366F1),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Libro de Recuerdos",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("book_close_btn")) {
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
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Book Mockup Showcase
                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(280.dp)
                        .shadow(16.dp, RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                        .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp, topStart = 4.dp, bottomStart = 4.dp))
                        .background(
                            Brush.verticalGradient(
                                colors = when (selectedFormat) {
                                    BookFormatOption.DIGITAL -> listOf(Color(0xFF0F766E), Color(0xFF14B8A6))
                                    BookFormatOption.SOFTCOVER -> listOf(Color(0xFF1D4ED8), Color(0xFF3B82F6))
                                    BookFormatOption.HARDCOVER -> listOf(Color(0xFF312E81), Color(0xFF4338CA), Color(0xFF6366F1))
                                }
                            )
                        )
                        .border(
                            2.dp,
                            if (selectedFormat == BookFormatOption.HARDCOVER) Color(0xFFFFD54F) else Color.White.copy(alpha = 0.5f),
                            RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp, topStart = 4.dp, bottomStart = 4.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(text = selectedFormat.iconEmoji, style = MaterialTheme.typography.titleLarge)
                            }
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (recipientName.isBlank()) "NUESTRA HISTORIA" else recipientName.uppercase(),
                                color = if (selectedFormat == BookFormatOption.HARDCOVER) Color(0xFFFFD54F) else Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Cápsula de Memorias",
                                color = Color.White.copy(alpha = 0.9f),
                                style = MaterialTheme.typography.labelMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "${memories.size} Recuerdos y bocetos incluidos",
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.labelSmall
                            )
                        }

                        Text(
                            text = selectedFormat.title.uppercase(),
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.labelSmall,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // Format Selection Cards (Digital vs Softcover vs Hardcover)
                Text(
                    text = "Elige tu formato preferido:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    BookFormatOption.values().forEach { option ->
                        val isSelected = selectedFormat == option
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            border = if (isSelected) {
                                androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                            } else {
                                androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                            },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                                else MaterialTheme.colorScheme.surface
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedFormat = option }
                                .testTag("format_${option.name}")
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = option.iconEmoji, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = option.title,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = option.subtitle,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = option.priceText,
                                        fontWeight = FontWeight.ExtraBold,
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    if (!option.isPhysical) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFF10B981)
                                        ) {
                                            Text(
                                                text = "INMEDIATO",
                                                color = Color.White,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Profit & Price Breakdown (Transparency)
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Formato: ${selectedFormat.title}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF14532D)
                            )
                            Text(
                                text = selectedFormat.priceText,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFF15803D)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Costo de producción:", style = MaterialTheme.typography.bodySmall, color = Color(0xFF166534))
                            Text(text = selectedFormat.printCost, style = MaterialTheme.typography.bodySmall, color = Color(0xFF166534))
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Tu Ganancia Neta:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = Color(0xFF15803D))
                            Text(text = selectedFormat.netProfit, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = Color(0xFF15803D))
                        }
                    }
                }

                // Order Simulator Form
                if (orderSubmitted) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF10B981).copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = if (!selectedFormat.isPhysical) "¡Archivo Digital Generado!" else "¡Pedido Físico Confirmado!",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46)
                            )
                            Text(
                                text = if (!selectedFormat.isPhysical)
                                    "Tu PDF / ePub en alta resolución de 300 DPI está listo para descargar o imprimir donde prefieras."
                                else
                                    "La orden ha sido enviada a la imprenta para fabricación y envío con guía de rastreo a tu domicilio.",
                                style = MaterialTheme.typography.bodySmall,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF047857)
                            )
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = recipientName,
                        onValueChange = { recipientName = it },
                        label = { Text("Nombre para la Portada / Dedicatoria") },
                        placeholder = { Text("Ej: Nuestra Familia, Para Mamá...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("book_recipient_input"),
                        singleLine = true
                    )

                    if (!selectedFormat.isPhysical) {
                        // Digital only needs email
                        OutlinedTextField(
                            value = emailAddress,
                            onValueChange = { emailAddress = it },
                            label = { Text("Correo Electrónico para Recibir el Archivo") },
                            placeholder = { Text("ejemplo@gmail.com") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("book_email_input"),
                            singleLine = true
                        )

                        Button(
                            onClick = { orderSubmitted = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("book_order_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.CloudDownload, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Generar y Descargar ePub / PDF ($5.99 USD)", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // Editorial Acuario Notice
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "📖 Editorial Acuario • Impresión Física",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Catálogo de impresión física en pasta dura y suave próximamente disponible en el portal web oficial. Mientras tanto, puedes descargar tu Libro Digital.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                }
                            }
                        }

                        // Physical needs shipping address
                        OutlinedTextField(
                            value = shippingAddress,
                            onValueChange = { shippingAddress = it },
                            label = { Text("Dirección de Envío Completa") },
                            placeholder = { Text("Calle, Número, Ciudad, Código Postal, País") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("book_address_input"),
                            minLines = 2
                        )

                        Button(
                            onClick = { orderSubmitted = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("book_order_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1)),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Default.LocalShipping, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Ordenar ${selectedFormat.title} (${selectedFormat.priceText})", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
