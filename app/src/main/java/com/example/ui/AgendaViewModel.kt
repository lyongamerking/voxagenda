package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AgendaDatabase
import com.example.data.AgendaEvent
import com.example.data.AgendaRepository
import com.example.receiver.AlarmReceiver
import com.example.voice.AlarmScheduler
import com.example.voice.VoiceAgentManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AgendaViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AgendaDatabase.getInstance(application)
    private val repository = AgendaRepository(database.agendaDao())
    val voiceAgentManager = VoiceAgentManager(application)
    val alarmScheduler = AlarmScheduler(application)

    val allEvents = repository.allEvents.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val memories = repository.memories.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _selectedDayEpoch = MutableStateFlow<Long?>(null)
    val selectedDayEpoch: StateFlow<Long?> = _selectedDayEpoch.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow("Todos")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    private val _selectedTab = MutableStateFlow(0) // 0: Agenda, 1: Recuerdos, 2: Agente
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    private val _activeAlarmEvent = MutableStateFlow<AgendaEvent?>(null)
    val activeAlarmEvent: StateFlow<AgendaEvent?> = _activeAlarmEvent.asStateFlow()

    private val _showAddEditDialog = MutableStateFlow(false)
    val showAddEditDialog: StateFlow<Boolean> = _showAddEditDialog.asStateFlow()

    private val _editingEvent = MutableStateFlow<AgendaEvent?>(null)
    val editingEvent: StateFlow<AgendaEvent?> = _editingEvent.asStateFlow()

    private val _showMarketDialog = MutableStateFlow(false)
    val showMarketDialog: StateFlow<Boolean> = _showMarketDialog.asStateFlow()

    // Monetization States
    private val _showVipDialog = MutableStateFlow(false)
    val showVipDialog: StateFlow<Boolean> = _showVipDialog.asStateFlow()

    private val _isVipActive = MutableStateFlow(false)
    val isVipActive: StateFlow<Boolean> = _isVipActive.asStateFlow()

    private val _showGiftDialog = MutableStateFlow(false)
    val showGiftDialog: StateFlow<Boolean> = _showGiftDialog.asStateFlow()

    private val _giftTargetEvent = MutableStateFlow<AgendaEvent?>(null)
    val giftTargetEvent: StateFlow<AgendaEvent?> = _giftTargetEvent.asStateFlow()

    private val _showMemoryBookDialog = MutableStateFlow(false)
    val showMemoryBookDialog: StateFlow<Boolean> = _showMemoryBookDialog.asStateFlow()

    private val _showQrDialog = MutableStateFlow(false)
    val showQrDialog: StateFlow<Boolean> = _showQrDialog.asStateFlow()

    // Filtered events
    val filteredEvents = combine(
        allEvents,
        _selectedDayEpoch,
        _selectedCategoryFilter
    ) { events, dayEpoch, cat ->
        var list = events
        if (dayEpoch != null) {
            val dayStart = dayEpoch
            val dayEnd = dayEpoch + 86400000L
            list = list.filter { it.dateTimeEpochMs in dayStart until dayEnd }
        }
        if (cat != "Todos") {
            list = list.filter { it.category.equals(cat, ignoreCase = true) }
        }
        list
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Today's events
    val todayEvents = allEvents.combine(_selectedDayEpoch) { events, _ ->
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val start = cal.timeInMillis
        val end = start + 86400000L
        events.filter { it.dateTimeEpochMs in start until end }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val upcomingEvents = allEvents.combine(_selectedDayEpoch) { events, _ ->
        val now = System.currentTimeMillis()
        events.filter { it.dateTimeEpochMs >= now }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Seed initial data
        viewModelScope.launch {
            repository.ensureInitialData()
        }

        // Listen for triggered system alarms
        viewModelScope.launch {
            AlarmReceiver.alarmEvents.collect { triggered ->
                val ev = repository.getEventById(triggered.eventId)
                if (ev != null) {
                    triggerAlarmModal(ev)
                }
            }
        }
    }

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    fun setSelectedDay(epoch: Long?) {
        _selectedDayEpoch.value = epoch
    }

    fun setSelectedCategory(cat: String) {
        _selectedCategoryFilter.value = cat
    }

    fun openAddDialog() {
        _editingEvent.value = null
        _showAddEditDialog.value = true
    }

    fun openEditDialog(event: AgendaEvent) {
        _editingEvent.value = event
        _showAddEditDialog.value = true
    }

    fun closeAddEditDialog() {
        _showAddEditDialog.value = false
        _editingEvent.value = null
    }

    fun openMarketDialog() {
        _showMarketDialog.value = true
    }

    fun closeMarketDialog() {
        _showMarketDialog.value = false
    }

    fun saveEvent(event: AgendaEvent) {
        viewModelScope.launch {
            if (event.id == 0L) {
                val newId = repository.insertEvent(event)
                val savedEvent = event.copy(id = newId)
                if (savedEvent.isAlarmEnabled) {
                    alarmScheduler.scheduleAlarm(savedEvent)
                }
            } else {
                repository.updateEvent(event)
                if (event.isAlarmEnabled) {
                    alarmScheduler.scheduleAlarm(event)
                } else {
                    alarmScheduler.cancelAlarm(event.id)
                }
            }
            closeAddEditDialog()
        }
    }

    fun deleteEvent(event: AgendaEvent) {
        viewModelScope.launch {
            alarmScheduler.cancelAlarm(event.id)
            repository.deleteEvent(event)
        }
    }

    fun toggleCompleted(event: AgendaEvent) {
        viewModelScope.launch {
            val updated = event.copy(isCompleted = !event.isCompleted)
            repository.updateEvent(updated)
        }
    }

    fun playDailyBriefing() {
        val today = todayEvents.value
        val upcoming = upcomingEvents.value
        val text = voiceAgentManager.buildDailyBriefing(today, upcoming)
        voiceAgentManager.speak(text, persona = "Cálida")
    }

    fun openVipDialog() {
        _showVipDialog.value = true
    }

    fun closeVipDialog() {
        _showVipDialog.value = false
    }

    fun toggleVipStatus(active: Boolean) {
        _isVipActive.value = active
    }

    fun openGiftDialog(event: AgendaEvent? = null) {
        _giftTargetEvent.value = event
        _showGiftDialog.value = true
    }

    fun closeGiftDialog() {
        _showGiftDialog.value = false
        _giftTargetEvent.value = null
    }

    fun openMemoryBookDialog() {
        _showMemoryBookDialog.value = true
    }

    fun closeMemoryBookDialog() {
        _showMemoryBookDialog.value = false
    }

    fun openQrDialog() {
        _showQrDialog.value = true
    }

    fun closeQrDialog() {
        _showQrDialog.value = false
    }

    fun speakEvent(event: AgendaEvent) {
        voiceAgentManager.speak(
            text = event.getEffectiveVoiceMessage(),
            persona = event.voicePersona
        )
    }

    fun speakMemory(memory: AgendaEvent) {
        val dateFormatter = SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "ES"))
        val dateStr = dateFormatter.format(Date(memory.dateTimeEpochMs))
        val text = buildString {
            append("Aura te narra este bello recuerdo: ${memory.title}. ")
            append("Registrado en la fecha $dateStr. ")
            if (memory.description.isNotBlank()) {
                append("Detalles: ${memory.description}. ")
            }
            if (memory.reflectionNote.isNotBlank()) {
                append("Tu nota especial dice: ${memory.reflectionNote}. ")
            }
            append("¡Qué maravilloso es preservar estos momentos en tu corazón!")
        }
        voiceAgentManager.speak(text, persona = "Poética")
    }

    fun previewVoice(text: String, persona: String) {
        voiceAgentManager.speak(text, persona = persona)
    }

    fun stopSpeaking() {
        voiceAgentManager.stop()
    }

    fun triggerAlarmModal(event: AgendaEvent) {
        _activeAlarmEvent.value = event
        // Speak voice message automatically
        voiceAgentManager.speak(
            text = event.getEffectiveVoiceMessage(),
            persona = event.voicePersona
        )
    }

    fun snoozeAlarm(event: AgendaEvent) {
        alarmScheduler.scheduleTestAlarm(event, delaySeconds = 600) // 10 minutes
        dismissAlarm()
    }

    fun dismissAlarm() {
        voiceAgentManager.stop()
        _activeAlarmEvent.value = null
    }

    fun handleQuickPrompt(prompt: String) {
        when (prompt) {
            "¿Qué tengo hoy?" -> {
                val today = todayEvents.value
                val text = if (today.isEmpty()) {
                    "Para hoy no tienes eventos agendados. ¡Tienes tiempo libre para ti!"
                } else {
                    "Hoy tienes ${today.size} evento(s): " + today.joinToString(", ") { it.title }
                }
                voiceAgentManager.speak(text, persona = "Cálida")
            }
            "¿Cuál es mi próximo evento?" -> {
                val up = upcomingEvents.value
                val text = if (up.isEmpty()) {
                    "No tienes próximos eventos en tu agenda. Puedes crear uno nuevo pulsando el botón más."
                } else {
                    val next = up.first()
                    val format = SimpleDateFormat("EEEE d 'de' MMMM", Locale("es", "ES"))
                    "Tu próximo evento es ${next.title}, programado para el ${format.format(Date(next.dateTimeEpochMs))}."
                }
                voiceAgentManager.speak(text, persona = "Motivadora")
            }
            "¿Cápsula de recuerdos?" -> {
                _selectedTab.value = 1
                val mems = memories.value
                val text = "Bienvenido a tu cápsula. Cuentas con ${mems.size} recuerdo(s) guardados. Revive cada dibujo y nota cuando lo desees."
                voiceAgentManager.speak(text, persona = "Poética")
            }
            "Dame un consejo del día" -> {
                val tips = listOf(
                    "Cada día es un lienzo en blanco. Recuerda que no se trata solo de cumplir fechas, sino de disfrutar los momentos.",
                    "La mejor forma de alcanzar grandes metas es celebrar cada pequeño avance. ¡Sigue adelante!",
                    "Un minuto de organización al despertar te regala una hora de paz durante el día. ¡Aura está contigo!"
                )
                voiceAgentManager.speak(tips.random(), persona = "Motivadora")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        voiceAgentManager.release()
    }
}
