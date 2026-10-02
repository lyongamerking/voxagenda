package com.example.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AgendaEvent
import com.example.ui.components.AddEditEventDialog
import com.example.ui.components.AgentBriefingCard
import com.example.ui.components.AgentCompanionView
import com.example.ui.components.AuraVipDialog
import com.example.ui.components.CalendarStrip
import com.example.ui.components.CategoriesList
import com.example.ui.components.EventCard
import com.example.ui.components.GiftAffiliateDialog
import com.example.ui.components.MarketInnovationDialog
import com.example.ui.components.MemoryBookDialog
import com.example.ui.components.MemoryCapsuleView
import com.example.ui.components.ShareQrDialog
import com.example.ui.components.VoiceAlarmDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaMainScreen(
    viewModel: AgendaViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val allEvents by viewModel.allEvents.collectAsStateWithLifecycle()
    val filteredEvents by viewModel.filteredEvents.collectAsStateWithLifecycle()
    val todayEvents by viewModel.todayEvents.collectAsStateWithLifecycle()
    val upcomingEvents by viewModel.upcomingEvents.collectAsStateWithLifecycle()
    val memories by viewModel.memories.collectAsStateWithLifecycle()

    val selectedDayEpoch by viewModel.selectedDayEpoch.collectAsStateWithLifecycle()
    val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsStateWithLifecycle()

    val isSpeaking by viewModel.voiceAgentManager.isSpeaking.collectAsStateWithLifecycle()
    val currentSpokenText by viewModel.voiceAgentManager.currentSpokenText.collectAsStateWithLifecycle()

    val activeAlarmEvent by viewModel.activeAlarmEvent.collectAsStateWithLifecycle()
    val showAddEditDialog by viewModel.showAddEditDialog.collectAsStateWithLifecycle()
    val editingEvent by viewModel.editingEvent.collectAsStateWithLifecycle()
    val showMarketDialog by viewModel.showMarketDialog.collectAsStateWithLifecycle()

    val showVipDialog by viewModel.showVipDialog.collectAsStateWithLifecycle()
    val isVipActive by viewModel.isVipActive.collectAsStateWithLifecycle()
    val showGiftDialog by viewModel.showGiftDialog.collectAsStateWithLifecycle()
    val giftTargetEvent by viewModel.giftTargetEvent.collectAsStateWithLifecycle()
    val showMemoryBookDialog by viewModel.showMemoryBookDialog.collectAsStateWithLifecycle()
    val showQrDialog by viewModel.showQrDialog.collectAsStateWithLifecycle()

    val filterOptions = listOf("Todos") + CategoriesList

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.GraphicEq,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "VoxAgenda",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    // QR Code Share button
                    IconButton(
                        onClick = { viewModel.openQrDialog() },
                        modifier = Modifier.testTag("topbar_qr_btn")
                    ) {
                        Icon(
                            Icons.Default.QrCode,
                            contentDescription = "Compartir con Código QR",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // VIP Club button
                    IconButton(
                        onClick = { viewModel.openVipDialog() },
                        modifier = Modifier.testTag("topbar_vip_btn")
                    ) {
                        if (isVipActive) {
                            Icon(
                                Icons.Default.WorkspacePremium,
                                contentDescription = "Aura VIP Activo",
                                tint = Color(0xFFFFB300)
                            )
                        } else {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF6366F1).copy(alpha = 0.15f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF6366F1).copy(alpha = 0.5f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.WorkspacePremium,
                                        contentDescription = null,
                                        tint = Color(0xFF6366F1),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "VIP",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF6366F1)
                                    )
                                }
                            }
                        }
                    }

                    IconButton(
                        onClick = { viewModel.openMarketDialog() },
                        modifier = Modifier.testTag("topbar_market_btn")
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = "Mercado y Negocio",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { viewModel.setSelectedTab(0) },
                    icon = { Icon(Icons.Default.EventNote, contentDescription = "Agenda") },
                    label = { Text("Agenda") },
                    modifier = Modifier.testTag("tab_agenda")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { viewModel.setSelectedTab(1) },
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Recuerdos") },
                    label = { Text("Recuerdos") },
                    modifier = Modifier.testTag("tab_memories")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { viewModel.setSelectedTab(2) },
                    icon = { Icon(Icons.Default.Psychology, contentDescription = "Agente") },
                    label = { Text("Agente Aura") },
                    modifier = Modifier.testTag("tab_agent")
                )
            }
        },
        floatingActionButton = {
            if (selectedTab != 2) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.openAddDialog() },
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nuevo Evento / Dibujo") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("fab_add_event")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> {
                    // Agenda Tab
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("agenda_list"),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Companion Hero Briefing Card
                        item {
                            AgentBriefingCard(
                                todayEvents = todayEvents,
                                upcomingEvents = upcomingEvents,
                                isSpeaking = isSpeaking,
                                currentSpokenText = currentSpokenText,
                                onPlayBriefing = { viewModel.playDailyBriefing() },
                                onStopSpeaking = { viewModel.stopSpeaking() },
                                onOpenMarketAnalysis = { viewModel.openMarketDialog() },
                                onQuickPrompt = { prompt -> viewModel.handleQuickPrompt(prompt) }
                            )
                        }

                        // Calendar Day Strip
                        item {
                            CalendarStrip(
                                selectedDayEpoch = selectedDayEpoch,
                                onSelectDay = { epoch -> viewModel.setSelectedDay(epoch) },
                                events = allEvents
                            )
                        }

                        // Category Filter Chips
                        item {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(filterOptions) { cat ->
                                    FilterChip(
                                        selected = selectedCategoryFilter == cat,
                                        onClick = { viewModel.setSelectedCategory(cat) },
                                        label = { Text(cat) },
                                        modifier = Modifier.testTag("filter_$cat")
                                    )
                                }
                            }
                        }

                        // Section Title
                        item {
                            Text(
                                text = if (selectedDayEpoch != null) {
                                    "Eventos del día seleccionado (${filteredEvents.size})"
                                } else {
                                    "Próximos Eventos y Fechas (${filteredEvents.size})"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Empty State if no events
                        if (filteredEvents.isEmpty()) {
                            item {
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Icon(
                                            Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(40.dp)
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = "No hay eventos con estos filtros",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Pulsa '+ Nuevo Evento / Dibujo' para crear una fecha importante con su dibujo y voz.",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        } else {
                            items(filteredEvents, key = { it.id }) { event ->
                                EventCard(
                                    event = event,
                                    onSpeakVoice = { viewModel.speakEvent(event) },
                                    onTriggerTestAlarm = { viewModel.triggerAlarmModal(event) },
                                    onToggleCompleted = { viewModel.toggleCompleted(event) },
                                    onEdit = { viewModel.openEditDialog(event) },
                                    onDelete = { viewModel.deleteEvent(event) },
                                    onSendGift = { viewModel.openGiftDialog(event) }
                                )
                            }
                        }

                        // Bottom branding & spacer for FAB
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp, bottom = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🦁 Lyon Studio",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Creando experiencias digitales con alma",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(72.dp))
                            }
                        }
                    }
                }
                1 -> {
                    // Memories Tab
                    Box(modifier = Modifier.padding(16.dp)) {
                        MemoryCapsuleView(
                            memories = memories,
                            onSpeakMemory = { mem -> viewModel.speakMemory(mem) },
                            onAddNewMemory = { viewModel.openAddDialog() },
                            onOpenMemoryBook = { viewModel.openMemoryBookDialog() }
                        )
                    }
                }
                2 -> {
                    // Agent Companion Tab
                    AgentCompanionView(
                        allEvents = allEvents,
                        memories = memories,
                        isSpeaking = isSpeaking,
                        onTestVoice = { text, persona -> viewModel.previewVoice(text, persona) },
                        onOpenMarketAnalysis = { viewModel.openMarketDialog() },
                        onAskAdvice = { viewModel.handleQuickPrompt("Dame un consejo del día") }
                    )
                }
            }
        }
    }

    // Add / Edit Dialog
    if (showAddEditDialog) {
        AddEditEventDialog(
            initialEvent = editingEvent,
            onSave = { savedEvent -> viewModel.saveEvent(savedEvent) },
            onDismiss = { viewModel.closeAddEditDialog() },
            onPreviewVoice = { text, persona -> viewModel.previewVoice(text, persona) }
        )
    }

    // Voice Alarm Dialog
    activeAlarmEvent?.let { alarmEvent ->
        VoiceAlarmDialog(
            event = alarmEvent,
            isSpeaking = isSpeaking,
            onSpeakAgain = { viewModel.speakEvent(alarmEvent) },
            onSnooze = { viewModel.snoozeAlarm(alarmEvent) },
            onDismiss = { viewModel.dismissAlarm() }
        )
    }

    // Market Analysis & Innovation Dialog
    if (showMarketDialog) {
        MarketInnovationDialog(onDismiss = { viewModel.closeMarketDialog() })
    }

    // Aura VIP Dialog
    if (showVipDialog) {
        AuraVipDialog(
            isVipActive = isVipActive,
            onToggleVip = { viewModel.toggleVipStatus(it) },
            onDismiss = { viewModel.closeVipDialog() }
        )
    }

    // Gift Affiliate Catalog Dialog
    if (showGiftDialog) {
        GiftAffiliateDialog(
            targetEvent = giftTargetEvent,
            onDismiss = { viewModel.closeGiftDialog() }
        )
    }

    // Memory Book Print-on-Demand Dialog
    if (showMemoryBookDialog) {
        MemoryBookDialog(
            memories = memories,
            onDismiss = { viewModel.closeMemoryBookDialog() }
        )
    }

    // Share QR Code Dialog
    if (showQrDialog) {
        ShareQrDialog(
            appUrl = "https://ais-pre-thjsmflrrydx6vex2rla2r-493362986173.us-west1.run.app",
            onDismiss = { viewModel.closeQrDialog() }
        )
    }
}
