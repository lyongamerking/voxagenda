package com.example.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DoodleDrawing
import com.example.data.DoodlePoint
import com.example.data.DoodleStamp
import com.example.data.DoodleStroke

val DoodleColors = listOf(
    "#F59E0B", // Amber Gold
    "#EC4899", // Rose Pink
    "#06B6D4", // Cyan
    "#A855F7", // Purple
    "#10B981", // Emerald
    "#EF4444", // Red Coral
    "#84CC16", // Lime
    "#FFFFFF"  // White
)

val DoodleStamps = listOf("🎂", "❤️", "⭐", "🎉", "✈️", "💍", "🏆", "🌸", "💡", "🎵")

fun parseHexColor(hex: String, fallback: Color = Color.White): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (_: Exception) {
        fallback
    }
}

/**
 * Compact thumbnail that renders a scaled representation of the user's doodle.
 */
@Composable
fun DoodleThumbnail(
    drawing: DoodleDrawing,
    modifier: Modifier = Modifier
) {
    val bgColor = parseHexColor(drawing.bgColorHex, Color(0xFF1E1B4B))

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height

            // Normalized against default 200x200 canvas
            val scaleX = canvasW / 200f
            val scaleY = canvasH / 200f
            val scale = minOf(scaleX, scaleY)

            for (stroke in drawing.strokes) {
                if (stroke.points.size < 2) continue
                val strokeColor = parseHexColor(stroke.colorHex)
                val path = Path()
                path.moveTo(stroke.points[0].x * scale, stroke.points[0].y * scale)
                for (i in 1 until stroke.points.size) {
                    val pt = stroke.points[i]
                    path.lineTo(pt.x * scale, pt.y * scale)
                }
                drawPath(
                    path = path,
                    color = strokeColor,
                    style = Stroke(
                        width = (stroke.strokeWidth * scale).coerceAtLeast(1.5f),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
            }

            // Draw stamps
            val paint = Paint().apply {
                isAntiAlias = true
                textAlign = Paint.Align.CENTER
            }
            for (stamp in drawing.stamps) {
                paint.textSize = (stamp.size * scale).coerceAtLeast(12f)
                drawContext.canvas.nativeCanvas.drawText(
                    stamp.symbol,
                    stamp.x * scale,
                    stamp.y * scale,
                    paint
                )
            }
        }
    }
}

/**
 * Full interactive drawing canvas pad for creating or editing doodles.
 */
@Composable
fun InteractiveDoodlePad(
    initialDrawing: DoodleDrawing,
    onDrawingChanged: (DoodleDrawing) -> Unit,
    modifier: Modifier = Modifier
) {
    val strokes = remember { mutableStateListOf<DoodleStroke>().apply { addAll(initialDrawing.strokes) } }
    val stamps = remember { mutableStateListOf<DoodleStamp>().apply { addAll(initialDrawing.stamps) } }

    var selectedColorHex by remember { mutableStateOf(DoodleColors[0]) }
    var selectedStrokeWidth by remember { mutableFloatStateOf(8f) }
    var selectedTool by remember { mutableStateOf("pen") } // "pen", "stamp", "eraser"
    var selectedStamp by remember { mutableStateOf(DoodleStamps[0]) }

    val currentPoints = remember { mutableStateListOf<DoodlePoint>() }

    fun emitChange() {
        onDrawingChanged(
            DoodleDrawing(
                strokes = strokes.toList(),
                stamps = stamps.toList(),
                bgColorHex = "#1E1B4B"
            )
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        // Toolbar: Tools + Undo + Clear
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                FilterChip(
                    selected = selectedTool == "pen",
                    onClick = { selectedTool = "pen" },
                    label = { Text("Pincel") },
                    leadingIcon = { Icon(Icons.Default.Brush, contentDescription = null, Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tool_pen")
                )
                FilterChip(
                    selected = selectedTool == "stamp",
                    onClick = { selectedTool = "stamp" },
                    label = { Text("Stickers") },
                    leadingIcon = { Icon(Icons.Default.AutoAwesome, contentDescription = null, Modifier.size(16.dp)) },
                    modifier = Modifier.testTag("tool_stamp")
                )
                FilterChip(
                    selected = selectedTool == "eraser",
                    onClick = { selectedTool = "eraser" },
                    label = { Text("Borrador") },
                    modifier = Modifier.testTag("tool_eraser")
                )
            }

            Row {
                IconButton(
                    onClick = {
                        if (strokes.isNotEmpty()) {
                            strokes.removeAt(strokes.lastIndex)
                            emitChange()
                        } else if (stamps.isNotEmpty()) {
                            stamps.removeAt(stamps.lastIndex)
                            emitChange()
                        }
                    },
                    modifier = Modifier.testTag("doodle_undo")
                ) {
                    Icon(Icons.Default.Undo, contentDescription = "Deshacer")
                }
                IconButton(
                    onClick = {
                        strokes.clear()
                        stamps.clear()
                        emitChange()
                    },
                    modifier = Modifier.testTag("doodle_clear")
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Borrar todo", tint = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Subtool bar: Colors & stroke widths if Pen, or Stamps if Stamp
        if (selectedTool == "pen") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Color choices
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(DoodleColors) { hex ->
                        val color = parseHexColor(hex)
                        val isSelected = selectedColorHex == hex
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f),
                                    shape = CircleShape
                                )
                                .clickable { selectedColorHex = hex }
                                .testTag("color_picker_$hex")
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Stroke width buttons
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf(4f to "Fino", 8f to "Medio", 16f to "Grueso").forEach { (width, label) ->
                        val isSelected = selectedStrokeWidth == width
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .border(
                                    1.dp,
                                    if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f),
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedStrokeWidth = width }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        } else if (selectedTool == "stamp") {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(DoodleStamps) { stampEmoji ->
                    val isSelected = selectedStamp == stampEmoji
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        tonalElevation = if (isSelected) 4.dp else 1.dp,
                        modifier = Modifier
                            .clickable { selectedStamp = stampEmoji }
                            .padding(2.dp)
                            .testTag("stamp_$stampEmoji")
                    ) {
                        Text(
                            text = stampEmoji,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Drawing Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.25f)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFF16152B))
                .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                .testTag("doodle_canvas_area")
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(selectedTool, selectedColorHex, selectedStrokeWidth, selectedStamp) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                if (selectedTool == "pen") {
                                    currentPoints.clear()
                                    currentPoints.add(DoodlePoint(offset.x, offset.y))
                                } else if (selectedTool == "stamp") {
                                    stamps.add(
                                        DoodleStamp(
                                            x = offset.x,
                                            y = offset.y,
                                            symbol = selectedStamp,
                                            size = 36f
                                        )
                                    )
                                    emitChange()
                                } else if (selectedTool == "eraser") {
                                    // Remove nearest stroke or stamp
                                    val strokeIdx = strokes.indexOfLast { stroke ->
                                        stroke.points.any { (it.x - offset.x) * (it.x - offset.x) + (it.y - offset.y) * (it.y - offset.y) < 600f }
                                    }
                                    if (strokeIdx >= 0) {
                                        strokes.removeAt(strokeIdx)
                                        emitChange()
                                    }
                                }
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                if (selectedTool == "pen") {
                                    currentPoints.add(DoodlePoint(change.position.x, change.position.y))
                                }
                            },
                            onDragEnd = {
                                if (selectedTool == "pen" && currentPoints.isNotEmpty()) {
                                    strokes.add(
                                        DoodleStroke(
                                            points = currentPoints.toList(),
                                            colorHex = selectedColorHex,
                                            strokeWidth = selectedStrokeWidth
                                        )
                                    )
                                    currentPoints.clear()
                                    emitChange()
                                }
                            }
                        )
                    }
            ) {
                // Draw existing strokes
                for (stroke in strokes) {
                    if (stroke.points.size < 2) continue
                    val strokeColor = parseHexColor(stroke.colorHex)
                    val path = Path()
                    path.moveTo(stroke.points[0].x, stroke.points[0].y)
                    for (i in 1 until stroke.points.size) {
                        path.lineTo(stroke.points[i].x, stroke.points[i].y)
                    }
                    drawPath(
                        path = path,
                        color = strokeColor,
                        style = Stroke(
                            width = stroke.strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                // Draw in-progress stroke
                if (currentPoints.size >= 2) {
                    val activeColor = parseHexColor(selectedColorHex)
                    val activePath = Path()
                    activePath.moveTo(currentPoints[0].x, currentPoints[0].y)
                    for (i in 1 until currentPoints.size) {
                        activePath.lineTo(currentPoints[i].x, currentPoints[i].y)
                    }
                    drawPath(
                        path = activePath,
                        color = activeColor,
                        style = Stroke(
                            width = selectedStrokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                // Draw stamps
                val textPaint = Paint().apply {
                    isAntiAlias = true
                    textAlign = Paint.Align.CENTER
                }
                for (stamp in stamps) {
                    textPaint.textSize = stamp.size
                    drawContext.canvas.nativeCanvas.drawText(
                        stamp.symbol,
                        stamp.x,
                        stamp.y,
                        textPaint
                    )
                }
            }

            if (strokes.isEmpty() && stamps.isEmpty() && currentPoints.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✍️ ¡Dibuja aquí tu evento o recuerdo!",
                        color = Color.White.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
