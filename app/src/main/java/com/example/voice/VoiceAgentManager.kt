package com.example.voice

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.data.AgendaEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VoiceAgentManager(context: Context) : TextToSpeech.OnInitListener {

    private val appContext = context.applicationContext
    private var tts: TextToSpeech? = null

    init {
        try {
            tts = TextToSpeech(appContext, this)
        } catch (e: Exception) {
            Log.e("VoiceAgentManager", "Error initializing TextToSpeech", e)
        }
    }

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentSpokenText = MutableStateFlow("")
    val currentSpokenText: StateFlow<String> = _currentSpokenText.asStateFlow()

    private var onDoneCallback: (() -> Unit)? = null

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale("es", "ES"))
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default locale
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }

                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                    val cb = onDoneCallback
                    onDoneCallback = null
                    cb?.invoke()
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                    val cb = onDoneCallback
                    onDoneCallback = null
                    cb?.invoke()
                }
            })
            _isReady.value = true
        } else {
            Log.e("VoiceAgentManager", "Initialization of TextToSpeech failed with status: $status")
        }
    }

    fun speak(
        text: String,
        persona: String = "Cálida",
        onDone: () -> Unit = {}
    ) {
        if (text.isBlank()) return
        onDoneCallback = onDone
        _currentSpokenText.value = text

        val pitch = when (persona) {
            "Motivadora" -> 1.15f
            "Ejecutiva" -> 0.92f
            "Poética" -> 0.88f
            else -> 1.0f // Cálida
        }

        val rate = when (persona) {
            "Motivadora" -> 1.05f
            "Ejecutiva" -> 1.0f
            "Poética" -> 0.85f
            else -> 0.95f // Cálida
        }

        tts?.setPitch(pitch)
        tts?.setSpeechRate(rate)

        val params = Bundle()
        val utteranceId = "vox_utterance_${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, params, utteranceId)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        onDoneCallback?.invoke()
        onDoneCallback = null
    }

    fun buildDailyBriefing(todayEvents: List<AgendaEvent>, upcomingEvents: List<AgendaEvent>): String {
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        if (todayEvents.isEmpty()) {
            if (upcomingEvents.isNotEmpty()) {
                val next = upcomingEvents.first()
                val nextTime = timeFormat.format(next.dateTimeEpochMs)
                return "¡Hola! Aura reportándose. Hoy no tienes eventos urgentes. Tu próximo evento es ${next.title} programado para las $nextTime. Es una gran oportunidad para relajarte o preparar tus metas."
            }
            return "¡Buenos días! Tu agente personal Aura te saluda. Hoy tu agenda está completamente despejada. Aprovecha el día para cuidar de ti, disfrutar y crear nuevos recuerdos."
        }

        val count = todayEvents.size
        val first = todayEvents[0]
        val firstTime = timeFormat.format(first.dateTimeEpochMs)

        return if (count == 1) {
            "¡Hola! Aura aquí. Hoy tienes un evento agendado: ${first.title} a las $firstTime. Te deseo el mayor de los éxitos en esta actividad."
        } else {
            val second = todayEvents[1]
            val secondTime = timeFormat.format(second.dateTimeEpochMs)
            "¡Buenos días! Tu asistente Aura te informa: para el día de hoy tienes $count compromisos. Primero: ${first.title} a las $firstTime, y luego ${second.title} a las $secondTime. ¡A por todas con energía positiva!"
        }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
