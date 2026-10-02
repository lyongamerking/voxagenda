package com.example.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.R
import com.example.data.AgendaEvent
import com.example.data.DoodleDrawing
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val CategoriesList = listOf(
    "Cumpleaños",
    "Aniversario",
    "Cita Médica",
    "Meta Personal",
    "Viaje",
    "Recuerdo",
    "Trabajo",
    "Familia"
)

val Personas = listOf(
    "Cálida" to "🌟 Cálida & Afectuosa",
    "Motivadora" to "🚀 Motivadora & Enérgica",
    "Ejecutiva" to "💼 Ejecutiva & Precisa",
    "Poética" to "🌸 Poética & Reflexiva"
)

val AiArtStylesList = listOf(
    "Celebración & Acuarela" to R.drawable.ai_art_celebration,
    "Paisaje & Escapada" to R.drawable.ai_art_scenic
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditEventDialog(
    initialEvent: AgendaEvent? = null,
    onSave: (AgendaEvent) -> Unit,
    onDismiss: () -> Unit,
    onPreviewVoice: (text: String, persona: String) -> Unit
) {
    val context = LocalContext.current

    var title by remember { mutableStateOf(initialEvent?.title ?: "") }
    var description by remember { mutableStateOf(initialEvent?.description ?: "") }
    var category by remember { mutableStateOf(initialEvent?.category ?: CategoriesList[0]) }
    var importance by remember { mutableStateOf(initialEvent?.importance ?: "Alta") }

    // 3 Visual options: "DOODLE", "PHOTO", "AI_ART"
    var visualType by remember { mutableStateOf(initialEvent?.visualType ?: "DOODLE") }
    var photoUriString by remember { mutableStateOf(initialEvent?.photoUri ?: "") }
    var aiArtStyle by remember { mutableStateOf(initialEvent?.aiArtStyle ?: "Celebración & Acuarela") }

    var voicePersona by remember { mutableStateOf(initialEvent?.voicePersona ?: "Cálida") }
    var customVoiceMessage by remember { mutableStateOf(initialEvent?.voiceMessageCustom ?: "") }
    var isAlarmEnabled by remember { mutableStateOf(initialEvent?.isAlarmEnabled ?: true) }
    var isMemoryCapsule by remember { mutableStateOf(initialEvent?.isMemoryCapsule ?: false) }
    var reflectionNote by remember { mutableStateOf(initialEvent?.reflectionNote ?: "") }

    var dateTimeEpochMs by remember {
        mutableLongStateOf(
            initialEvent?.dateTimeEpochMs ?: (System.currentTimeMillis() + 3600000L)
        )
    }

    var doodleDrawing by remember {
        mutableStateOf(initialEvent?.getDoodle() ?: DoodleDrawing())
    }

    // Android Photo Picker Launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUriString = uri.toString()
            visualType = "PHOTO"
        }
    }

    val cal = remember(dateTimeEpochMs) {
        Calendar.getInstance().apply { timeInMillis = dateTimeEpochMs }
    }

    val dateFormatter = remember { SimpleDateFormat("EEEE, d 'de' MMMM, yyyy", Locale.getDefault()) }
    val timeFormatter = remember { SimpleDateFormat("h:mm a", Locale.getDefault()) }

    fun showDatePicker() {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance().apply {
                    timeInMillis = dateTimeEpochMs
                    set(Calendar.YEAR, year)
                    set(Calendar.MONTH, month)
                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                }
                dateTimeEpochMs = c.timeInMillis
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun showTimePicker() {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val c = Calendar.getInstance().apply {
                    timeInMillis = dateTimeEpochMs
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }
                dateTimeEpochMs = c.timeInMillis
            },
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            false
        ).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = if (initialEvent == null) "Nuevo Evento o Recuerdo" else "Editar Evento",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss, modifier = Modifier.testTag("dialog_close_btn")) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    },
                    actions = {
                        Button(
                            onClick = {
                                if (title.isNotBlank()) {
                                    val eventToSave = (initialEvent ?: AgendaEvent(
                                        title = title.trim(),
                                        dateTimeEpochMs = dateTimeEpochMs
                                    )).copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        dateTimeEpochMs = dateTimeEpochMs,
                                        category = category,
                                        importance = importance,
                                        visualType = visualType,
                                        doodleJson = doodleDrawing.toJson(),
                                        photoUri = photoUriString,
                                        aiArtStyle = aiArtStyle,
                                        voiceMessageCustom = customVoiceMessage.trim(),
                                        voicePersona = voicePersona,
                                        isAlarmEnabled = isAlarmEnabled,
                                        isMemoryCapsule = isMemoryCapsule,
                                        reflectionNote = reflectionNote.trim()
                                    )
                                    onSave(eventToSave)
                                }
                            },
                            enabled = title.isNotBlank(),
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .testTag("dialog_save_btn")
                        ) {
                            Text("Guardar")
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
                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nombre del Evento o Recuerdo *") },
                    placeholder = { Text("Ej: Cumpleaños de mamá, Viaje a la playa, Aniversario...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_title_input"),
                    singleLine = true
                )

                // Category chips
                Column {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(CategoriesList) { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat) },
                                modifier = Modifier.testTag("cat_chip_$cat")
                            )
                        }
                    }
                }

                // Date & Time pickers
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Fecha y Hora del Evento",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showDatePicker() },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .testTag("pick_date_btn")
                            ) {
                                Icon(Icons.Default.CalendarToday, contentDescription = null, Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = dateFormatter.format(Date(dateTimeEpochMs)),
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1
                                )
                            }
                            OutlinedButton(
                                onClick = { showTimePicker() },
                                modifier = Modifier
                                    .weight(0.9f)
                                    .testTag("pick_time_btn")
                            ) {
                                Icon(Icons.Default.AccessTime, contentDescription = null, Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = timeFormatter.format(Date(dateTimeEpochMs)),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    }
                }

                // ==========================================
                // 3 VISUAL OPTIONS: DOODLE / PHOTO / AI ART
                // ==========================================
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🖼️ Imagen o Arte del Evento",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = "Elige cómo quieres ilustrar este momento especial:",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Visual Type Selector Tabs
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FilterChip(
                                selected = visualType == "DOODLE",
                                onClick = { visualType = "DOODLE" },
                                label = { Text("🎨 Dibujo a Mano") },
                                modifier = Modifier.weight(1f).testTag("tab_visual_doodle")
                            )
                            FilterChip(
                                selected = visualType == "PHOTO",
                                onClick = { visualType = "PHOTO" },
                                label = { Text("📷 Foto Real") },
                                modifier = Modifier.weight(1f).testTag("tab_visual_photo")
                            )
                            FilterChip(
                                selected = visualType == "AI_ART",
                                onClick = { visualType = "AI_ART" },
                                label = { Text("✨ Boceto IA") },
                                modifier = Modifier.weight(1f).testTag("tab_visual_ai")
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        when (visualType) {
                            "DOODLE" -> {
                                // Manual Drawing Pad
                                Text(
                                    text = "Dibuja libremente con tu dedo o añade sellos:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                InteractiveDoodlePad(
                                    initialDrawing = doodleDrawing,
                                    onDrawingChanged = { updated ->
                                        doodleDrawing = updated
                                    }
                                )
                            }
                            "PHOTO" -> {
                                // Photo Picker from Gallery
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if (photoUriString.isNotBlank()) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                                        ) {
                                            AsyncImage(
                                                model = ImageRequest.Builder(context)
                                                    .data(Uri.parse(photoUriString))
                                                    .crossfade(true)
                                                    .build(),
                                                contentDescription = "Foto seleccionada",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(10.dp))
                                        FilledTonalButton(
                                            onClick = {
                                                photoPickerLauncher.launch(
                                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                )
                                            },
                                            modifier = Modifier.testTag("btn_change_photo")
                                        ) {
                                            Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Cambiar Foto de la Galería")
                                        }
                                    } else {
                                        // Empty photo state
                                        Surface(
                                            shape = RoundedCornerShape(16.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    photoPickerLauncher.launch(
                                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                                    )
                                                }
                                                .padding(vertical = 28.dp)
                                                .testTag("btn_pick_photo_placeholder")
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Icon(
                                                    Icons.Default.AddPhotoAlternate,
                                                    contentDescription = null,
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(44.dp)
                                                )
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "Toca para elegir una foto de tu galería",
                                                    fontWeight = FontWeight.Bold,
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Text(
                                                    text = "Foto de mamá, aniversario, viaje, amigos...",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            "AI_ART" -> {
                                // AI Art Generator & Style Selector
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Text(
                                        text = "Elige un estilo artístico generado para este evento:",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))

                                    LazyRow(
                                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        items(AiArtStylesList) { (styleName, drawableRes) ->
                                            val isSelected = aiArtStyle == styleName
                                            Card(
                                                shape = RoundedCornerShape(16.dp),
                                                border = if (isSelected) {
                                                    androidx.compose.foundation.BorderStroke(3.dp, MaterialTheme.colorScheme.primary)
                                                } else {
                                                    androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                                },
                                                modifier = Modifier
                                                    .width(160.dp)
                                                    .clickable { aiArtStyle = styleName }
                                                    .testTag("ai_style_$styleName")
                                            ) {
                                                Column {
                                                    Image(
                                                        painter = painterResource(id = drawableRes),
                                                        contentDescription = styleName,
                                                        contentScale = ContentScale.Crop,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .height(110.dp)
                                                    )
                                                    Text(
                                                        text = styleName,
                                                        style = MaterialTheme.typography.labelMedium,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                                        modifier = Modifier.padding(8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    val currentAiDrawable = when {
                                        aiArtStyle.contains("Paisaje", ignoreCase = true) -> R.drawable.ai_art_scenic
                                        else -> R.drawable.ai_art_celebration
                                    }

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(200.dp)
                                            .clip(RoundedCornerShape(16.dp))
                                            .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                                    ) {
                                        Image(
                                            painter = painterResource(id = currentAiDrawable),
                                            contentDescription = "Vista previa arte IA",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(topStart = 12.dp),
                                            color = Color.Black.copy(alpha = 0.7f),
                                            modifier = Modifier.align(Alignment.BottomEnd)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    Icons.Default.AutoAwesome,
                                                    contentDescription = null,
                                                    tint = Color(0xFFFFD54F),
                                                    modifier = Modifier.size(14.dp)
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = "Vista previa en alta calidad",
                                                    color = Color.White,
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.AutoAwesome,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Estilo: $aiArtStyle. Se adaptará e ilustrará '${if (title.isBlank()) "tu evento" else title}'.",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Voice Alarm & Personal Agent Configuration
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Alarma con Voz de Agente",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "El agente te hablará en voz alta cuando llegue la fecha",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = isAlarmEnabled,
                                onCheckedChange = { isAlarmEnabled = it },
                                modifier = Modifier.testTag("alarm_switch")
                            )
                        }

                        if (isAlarmEnabled) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Personalidad de la Voz:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Personas.forEach { (key, label) ->
                                    FilterChip(
                                        selected = voicePersona == key,
                                        onClick = { voicePersona = key },
                                        label = { Text(label) },
                                        modifier = Modifier.testTag("persona_$key")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = customVoiceMessage,
                                onValueChange = { customVoiceMessage = it },
                                label = { Text("Mensaje de voz personalizado (opcional)") },
                                placeholder = {
                                    Text("Si lo dejas vacío, el agente creará un mensaje automático.")
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("custom_voice_input"),
                                minLines = 2
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = {
                                    val demoEvent = AgendaEvent(
                                        title = if (title.isBlank()) "Mi Evento" else title,
                                        dateTimeEpochMs = dateTimeEpochMs,
                                        voicePersona = voicePersona,
                                        voiceMessageCustom = customVoiceMessage
                                    )
                                    onPreviewVoice(demoEvent.getEffectiveVoiceMessage(), voicePersona)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("preview_voice_btn")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("🔊 Probar cómo sonará la voz")
                            }
                        }
                    }
                }

                // Memory Capsule toggle
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                    ),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Guardar en Cápsula de Recuerdos",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "Preserva este evento en el cofre de memorias nostálgicas",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = isMemoryCapsule,
                                onCheckedChange = { isMemoryCapsule = it },
                                modifier = Modifier.testTag("capsule_switch")
                            )
                        }

                        if (isMemoryCapsule) {
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedTextField(
                                value = reflectionNote,
                                onValueChange = { reflectionNote = it },
                                label = { Text("Nota de recuerdo o reflexión emocional") },
                                placeholder = { Text("¿Qué hace único a este recuerdo? ¿Cómo te sentiste?") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("reflection_note_input"),
                                minLines = 2
                            )
                        }
                    }
                }

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detalles o notas adicionales") },
                    placeholder = { Text("Cosas por hacer, personas involucradas, ubicación...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("event_desc_input"),
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
