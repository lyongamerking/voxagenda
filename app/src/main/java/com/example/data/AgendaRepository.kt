package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Calendar

class AgendaRepository(private val dao: AgendaDao) {

    val allEvents: Flow<List<AgendaEvent>> = dao.getAllEventsFlow()
    val memories: Flow<List<AgendaEvent>> = dao.getMemoriesFlow()

    suspend fun getEventById(id: Long): AgendaEvent? = dao.getEventById(id)

    suspend fun insertEvent(event: AgendaEvent): Long = dao.insertEvent(event)

    suspend fun updateEvent(event: AgendaEvent) = dao.updateEvent(event)

    suspend fun deleteEvent(event: AgendaEvent) = dao.deleteEvent(event)

    suspend fun deleteEventById(id: Long) = dao.deleteEventById(id)

    suspend fun ensureInitialData() {
        if (dao.countEvents() == 0) {
            val now = System.currentTimeMillis()
            val cal = Calendar.getInstance()

            // 1. Mom's birthday tomorrow at 09:00 AM
            cal.add(Calendar.DAY_OF_YEAR, 1)
            cal.set(Calendar.HOUR_OF_DAY, 9)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.SECOND, 0)
            val cakeEvent = AgendaEvent(
                title = "Cumpleaños de Mamá 🎂",
                description = "Comprar flores, pastel de fresas y cantarle las mañanitas en familia.",
                dateTimeEpochMs = cal.timeInMillis,
                category = "Cumpleaños",
                importance = "Prioritaria",
                doodleJson = DoodleDrawing.createSampleCake().toJson(),
                voiceMessageCustom = "¡Hola! Hoy es el cumpleaños de mamá. ¡No olvides felicitarla con todo tu amor y llevarle su pastel favorito!",
                voicePersona = "Cálida",
                isAlarmEnabled = true,
                isCompleted = false,
                isMemoryCapsule = true,
                reflectionNote = "Siempre alegre, la mujer más especial de mi vida."
            )

            // 2. Beach Trip this weekend
            cal.timeInMillis = now
            cal.add(Calendar.DAY_OF_YEAR, 4)
            cal.set(Calendar.HOUR_OF_DAY, 7)
            cal.set(Calendar.MINUTE, 30)
            val beachEvent = AgendaEvent(
                title = "Escapada a la Playa 🏖️",
                description = "Fin de semana de descanso frente al mar con amigos.",
                dateTimeEpochMs = cal.timeInMillis,
                category = "Viaje",
                importance = "Alta",
                doodleJson = DoodleDrawing.createSampleBeach().toJson(),
                voiceMessageCustom = "¡Prepara las gafas de sol y el protector! Nos vamos a disfrutar del mar y la brisa.",
                voicePersona = "Motivadora",
                isAlarmEnabled = true,
                isCompleted = false,
                isMemoryCapsule = true,
                reflectionNote = "Un momento para desconectar de la rutina y reconectar con la naturaleza."
            )

            // 3. Goal Achievement / Graduation
            cal.timeInMillis = now
            cal.set(Calendar.HOUR_OF_DAY, 18)
            cal.set(Calendar.MINUTE, 0)
            val goalEvent = AgendaEvent(
                title = "Presentación de Gran Proyecto 🏆",
                description = "Entrega final y celebración con el equipo de trabajo.",
                dateTimeEpochMs = cal.timeInMillis,
                category = "Meta Personal",
                importance = "Alta",
                doodleJson = DoodleDrawing.createSampleGraduation().toJson(),
                voiceMessageCustom = "¡Momento crucial! Tu esfuerzo de semanas da frutos hoy. ¡Confía en tu talento y brilla!",
                voicePersona = "Ejecutiva",
                isAlarmEnabled = true,
                isCompleted = false,
                isMemoryCapsule = false
            )

            dao.insertEvent(cakeEvent)
            dao.insertEvent(beachEvent)
            dao.insertEvent(goalEvent)
        }
    }
}
