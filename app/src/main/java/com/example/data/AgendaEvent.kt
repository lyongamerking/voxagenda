package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "agenda_events")
data class AgendaEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String = "",
    val dateTimeEpochMs: Long,
    val category: String = "Especial",
    val importance: String = "Alta",
    val visualType: String = "DOODLE", // "DOODLE", "PHOTO", "AI_ART"
    val doodleJson: String = "{}",
    val photoUri: String = "",
    val aiArtStyle: String = "",
    val voiceMessageCustom: String = "",
    val voicePersona: String = "Cálida",
    val isAlarmEnabled: Boolean = true,
    val isCompleted: Boolean = false,
    val isMemoryCapsule: Boolean = false,
    val reflectionNote: String = "",
    val creationTimestamp: Long = System.currentTimeMillis()
) {
    fun getDoodle(): DoodleDrawing = DoodleDrawing.fromJson(doodleJson)

    fun getEffectiveVoiceMessage(): String {
        if (voiceMessageCustom.isNotBlank()) {
            return voiceMessageCustom
        }
        return when (voicePersona) {
            "Motivadora" -> "¡Atención! Aura reportándose. Ha llegado el momento de $title. ¡A darlo todo con la mejor actitud y energía!"
            "Ejecutiva" -> "Recordatorio de agenda: $title. Importancia $importance. Por favor verifique sus preparativos a tiempo."
            "Poética" -> "Un instante memorable florece en tu día: $title. Disfruta cada segundo y guárdalo en tu corazón."
            else -> "¡Hola! Soy tu asistente Aura. Te recuerdo con mucho cariño tu evento especial: $title. ¡Que tengas un día grandioso!"
        }
    }
}
