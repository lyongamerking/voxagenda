package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketInnovationDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Estudio de Mercado e Innovación",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("close_market_dialog")) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }
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
                // Intro Banner
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "¿Qué existe en el mercado y cómo innovar?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Análisis comparativo de competidores y estrategias ganadoras para hacer que este agente sea único en el mundo.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 1. Existing market products
                Text(
                    text = "1. Estado Actual del Mercado",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                val competitors = listOf(
                    Triple(
                        "Google Calendar / Apple Calendar",
                        "El estándar dominante en empresas y smartphones.",
                        "Deficiencias: Son rígidos, impersonales y fríos. Sus alarmas son pitidos monótonos que la gente suele posponer o ignorar. Cero dibujo creativo ni memoria emocional."
                    ),
                    Triple(
                        "TimeTree / FamCal",
                        "Líderes en calendarios compartidos para parejas y familias.",
                        "Deficiencias: Se limitan a cajas de texto y colores planos. No cuentan con un agente conversacional que te hable por voz ni un lienzo de dibujo íntimo."
                    ),
                    Triple(
                        "Notion / Todoist / TickTick",
                        "Herramientas de productividad y listas de tareas.",
                        "Deficiencias: Curva de aprendizaje alta. La experiencia es analítica de trabajo, no de recuerdos humanos ni fechas sentimentales."
                    ),
                    Triple(
                        "Finch / Pi / Habitica",
                        "Compañeros virtuales y mascotas gamificadas.",
                        "Deficiencias: Enfocados en autocuidado o chat general, no operan como una agenda formal de fechas importantes con alarmas exactas habladas."
                    )
                )

                competitors.forEach { (name, desc, flaw) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = flaw,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 2. How to innovate & dominate
                Text(
                    text = "2. Cómo Superar a la Competencia (Tu Ventaja)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                val pillars = listOf(
                    Pair(
                        "🎙️ 1. Alarmas con Voz Humana y Personalidad",
                        "En lugar de una alarma estresante, el agente te habla en voz alta adaptándose al tono (Cálido, Motivador, Ejecutivo o Poético): '¡Hola! Hoy es el cumpleaños de mamá, ¡acuérdate de felicitarla con todo tu cariño!'."
                    ),
                    Pair(
                        "🎨 2. 'Dibuja el Evento' (Vínculo Táctil)",
                        "La neurociencia demuestra que el ser humano recuerda 6 veces más aquello que dibuja con su mano. Cada fecha tiene su trazo, icono o boceto original del usuario."
                    ),
                    Pair(
                        "🕰️ 3. Cápsula de Recuerdos Dual",
                        "Una agenda normal solo mira al futuro y se desecha al pasar. VoxAgenda convierte cada evento cumplido en una cápsula nostálgica con notas de reflexión."
                    ),
                    Pair(
                        "☕ 4. Briefing Matutino Proactivo",
                        "El usuario no tiene que abrir menús complejos: presiona un botón mientras toma su café y el agente le relata su día completo en 25 segundos."
                    )
                )

                pillars.forEach { (title, detail) ->
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = detail, style = MaterialTheme.typography.bodySmall, lineHeight = 20.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 3. Roadmap for extreme scalability
                Text(
                    text = "3. Hoja de Ruta para Escalar al Máximo",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Estrategias de Crecimiento:", fontWeight = FontWeight.Bold)
                        }
                        Text("• 🗣️ Clonación de voz: Permitir que un familiar (pareja, madre, abuela) grabe una muestra para que sus voces te recuerden las fechas importantes.", style = MaterialTheme.typography.bodySmall)
                        Text("• 🪄 Foto a Boceto: Convertir una foto real en trazo de acuarela o dibujo artístico automático.", style = MaterialTheme.typography.bodySmall)
                        Text("• 🎁 Asistente de Detalles: Sugerencia inteligente de flores, regalos o cartas para aniversarios y cumpleaños.", style = MaterialTheme.typography.bodySmall)
                        Text("• 📱 Widget de Pantalla de Inicio: Mostrar el dibujo o foto del día con botón de escucha rápida.", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 4. Monetization Strategy
                Text(
                    text = "4. Plan de Monetización y Negocio",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "💡 ¿Cómo genera dinero esta aplicación?",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleSmall
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "1. Suscripción Freemium (Aura VIP - $3.99/mes o $29.99/año):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                            Text(text = "• Gratis: Eventos ilimitados, fotos de galería, dibujos manuales y voz estándar.", style = MaterialTheme.typography.bodySmall)
                            Text(text = "• VIP: Bocetos ilimitados con IA, voces ultra-realistas, clonación de voz y respaldo en la nube.", style = MaterialTheme.typography.bodySmall)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "2. Comisiones por Regalos y Experiencias (8% - 15%):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
                            Text(text = "• La app conoce las fechas clave con anticipación (aniversario, cumple de mamá). El agente puede sugerir reservar flores, chocolates o cenas con 1 toque mediante afiliación (Amazon, floristerías o Uber Eats).", style = MaterialTheme.typography.bodySmall)
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(text = "3. Álbum Físico de Recuerdos (Print-on-demand):", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF10B981))
                            Text(text = "• Al final del año, el usuario puede pedir la impresión en tapa dura de su 'Cápsula Anual' con sus dibujos, fotos y reflexiones para regalar a su familia.", style = MaterialTheme.typography.bodySmall)
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ Qué NO hacer: No colocar banners de publicidad intrusiva. En apps sentimentales, la publicidad rompe la confianza y el afecto con el agente personal.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("btn_close_market_bottom")
                ) {
                    Text("¡Excelente! Entendido")
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
