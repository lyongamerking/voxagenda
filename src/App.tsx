import React, { useEffect, useMemo, useRef, useState } from 'react';
import {
  AudioWaveform,
  Award,
  Brain,
  Calendar,
  CalendarCheck2,
  Heart,
  Lightbulb,
  Plus,
  QrCode,
} from 'lucide-react';
import {
  AgendaEvent,
  CategoriesList,
  createInitialSeedEvents,
  getEffectiveVoiceMessage,
} from './types/agenda';
import { voiceAgent } from './services/voiceAgent';
import { AgentBriefingCard } from './components/AgentBriefingCard';
import { CalendarStrip } from './components/CalendarStrip';
import { EventCard } from './components/EventCard';
import { MemoryCapsuleView } from './components/MemoryCapsuleView';
import { AgentCompanionView } from './components/AgentCompanionView';
import { AddEditEventDialog } from './components/AddEditEventDialog';
import { VoiceAlarmDialog } from './components/VoiceAlarmDialog';
import { MarketInnovationDialog } from './components/MarketInnovationDialog';
import { AuraVipDialog } from './components/AuraVipDialog';
import { GiftAffiliateDialog } from './components/GiftAffiliateDialog';
import { MemoryBookDialog } from './components/MemoryBookDialog';
import { ShareQrDialog } from './components/ShareQrDialog';

const STORAGE_KEY_EVENTS = 'voxagenda_events_v1';
const STORAGE_KEY_VIP = 'voxagenda_vip_v1';

export function App() {
  const [allEvents, setAllEvents] = useState<AgendaEvent[]>(() => {
    try {
      const saved = localStorage.getItem(STORAGE_KEY_EVENTS);
      if (saved) {
        const parsed = JSON.parse(saved);
        if (Array.isArray(parsed) && parsed.length > 0) {
          return parsed;
        }
      }
    } catch {
      // ignore
    }
    const seeded = createInitialSeedEvents();
    try {
      localStorage.setItem(STORAGE_KEY_EVENTS, JSON.stringify(seeded));
    } catch {
      // ignore
    }
    return seeded;
  });

  const [isVipActive, setIsVipActive] = useState<boolean>(() => {
    try {
      return localStorage.getItem(STORAGE_KEY_VIP) === 'true';
    } catch {
      return false;
    }
  });

  const [selectedTab, setSelectedTab] = useState<number>(0); // 0: Agenda, 1: Recuerdos, 2: Agente
  const [selectedDayEpoch, setSelectedDayEpoch] = useState<number | null>(null);
  const [selectedCategoryFilter, setSelectedCategoryFilter] = useState<string>('Todos');

  const [isSpeaking, setIsSpeaking] = useState<boolean>(false);
  const [currentSpokenText, setCurrentSpokenText] = useState<string>('');

  const [activeAlarmEvent, setActiveAlarmEvent] = useState<AgendaEvent | null>(null);
  const [showAddEditDialog, setShowAddEditDialog] = useState<boolean>(false);
  const [editingEvent, setEditingEvent] = useState<AgendaEvent | null>(null);
  const [showMarketDialog, setShowMarketDialog] = useState<boolean>(false);
  const [showVipDialog, setShowVipDialog] = useState<boolean>(false);
  const [showGiftDialog, setShowGiftDialog] = useState<boolean>(false);
  const [giftTargetEvent, setGiftTargetEvent] = useState<AgendaEvent | null>(null);
  const [showMemoryBookDialog, setShowMemoryBookDialog] = useState<boolean>(false);
  const [showQrDialog, setShowQrDialog] = useState<boolean>(false);

  const triggeredAlarmIdsRef = useRef<Set<string>>(new Set());

  // Sync events to localStorage
  useEffect(() => {
    try {
      localStorage.setItem(STORAGE_KEY_EVENTS, JSON.stringify(allEvents));
    } catch {
      // ignore
    }
  }, [allEvents]);

  // Sync VIP status to localStorage
  useEffect(() => {
    try {
      localStorage.setItem(STORAGE_KEY_VIP, String(isVipActive));
    } catch {
      // ignore
    }
  }, [isVipActive]);

  // Subscribe to voiceAgent state
  useEffect(() => {
    return voiceAgent.subscribe((speaking, text) => {
      setIsSpeaking(speaking);
      setCurrentSpokenText(text);
    });
  }, []);

  // Background check for scheduled alarms
  useEffect(() => {
    const interval = window.setInterval(() => {
      const now = Date.now();
      for (const ev of allEvents) {
        if (!ev.isAlarmEnabled || ev.isCompleted) continue;
        const diff = now - ev.dateTimeEpochMs;
        const key = `${ev.id}_${ev.dateTimeEpochMs}`;
        if (diff >= 0 && diff < 45000 && !triggeredAlarmIdsRef.current.has(key)) {
          triggeredAlarmIdsRef.current.add(key);
          triggerAlarmModal(ev);
          break;
        }
      }
    }, 5000);
    return () => clearInterval(interval);
  }, [allEvents]);

  const sortedEvents = useMemo(
    () => [...allEvents].sort((a, b) => a.dateTimeEpochMs - b.dateTimeEpochMs),
    [allEvents]
  );

  const memories = useMemo(
    () =>
      allEvents
        .filter((ev) => ev.isMemoryCapsule)
        .sort((a, b) => b.dateTimeEpochMs - a.dateTimeEpochMs),
    [allEvents]
  );

  const filteredEvents = useMemo(() => {
    let list = sortedEvents;
    if (selectedDayEpoch !== null) {
      const dayStart = selectedDayEpoch;
      const dayEnd = selectedDayEpoch + 86400000;
      list = list.filter((ev) => ev.dateTimeEpochMs >= dayStart && ev.dateTimeEpochMs < dayEnd);
    }
    if (selectedCategoryFilter !== 'Todos') {
      list = list.filter(
        (ev) => ev.category.toLowerCase() === selectedCategoryFilter.toLowerCase()
      );
    }
    return list;
  }, [sortedEvents, selectedDayEpoch, selectedCategoryFilter]);

  const todayEvents = useMemo(() => {
    const now = new Date();
    const start = new Date(now.getFullYear(), now.getMonth(), now.getDate(), 0, 0, 0, 0).getTime();
    const end = start + 86400000;
    return sortedEvents.filter((ev) => ev.dateTimeEpochMs >= start && ev.dateTimeEpochMs < end);
  }, [sortedEvents]);

  const upcomingEvents = useMemo(() => {
    const now = Date.now();
    return sortedEvents.filter((ev) => ev.dateTimeEpochMs >= now);
  }, [sortedEvents]);

  const filterOptions = useMemo(() => ['Todos', ...CategoriesList], []);

  const openAddDialog = () => {
    setEditingEvent(null);
    setShowAddEditDialog(true);
  };

  const openEditDialog = (event: AgendaEvent) => {
    setEditingEvent(event);
    setShowAddEditDialog(true);
  };

  const closeAddEditDialog = () => {
    setShowAddEditDialog(false);
    setEditingEvent(null);
  };

  const saveEvent = (event: AgendaEvent) => {
    if (event.id === 0) {
      const newId = allEvents.reduce((max, e) => Math.max(max, e.id), 0) + 1;
      const savedEvent: AgendaEvent = { ...event, id: newId };
      setAllEvents((prev) => [...prev, savedEvent]);
    } else {
      setAllEvents((prev) => prev.map((e) => (e.id === event.id ? event : e)));
    }
    closeAddEditDialog();
  };

  const deleteEvent = (event: AgendaEvent) => {
    setAllEvents((prev) => prev.filter((e) => e.id !== event.id));
  };

  const toggleCompleted = (event: AgendaEvent) => {
    setAllEvents((prev) =>
      prev.map((e) => (e.id === event.id ? { ...e, isCompleted: !e.isCompleted } : e))
    );
  };

  const playDailyBriefing = () => {
    const text = voiceAgent.buildDailyBriefing(todayEvents, upcomingEvents);
    voiceAgent.speak(text, 'Cálida');
  };

  const speakEvent = (event: AgendaEvent) => {
    voiceAgent.speak(getEffectiveVoiceMessage(event), event.voicePersona);
  };

  const speakMemory = (memory: AgendaEvent) => {
    const dateStr = new Intl.DateTimeFormat('es-ES', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    }).format(new Date(memory.dateTimeEpochMs));

    let text = `Aura te narra este bello recuerdo: ${memory.title}. Registrado en la fecha ${dateStr}. `;
    if (memory.description.trim()) {
      text += `Detalles: ${memory.description}. `;
    }
    if (memory.reflectionNote.trim()) {
      text += `Tu nota especial dice: ${memory.reflectionNote}. `;
    }
    text += '¡Qué maravilloso es preservar estos momentos en tu corazón!';

    voiceAgent.speak(text, 'Poética');
  };

  const previewVoice = (text: string, persona: string) => {
    voiceAgent.speak(text, persona);
  };

  const stopSpeaking = () => {
    voiceAgent.stop();
  };

  const triggerAlarmModal = (event: AgendaEvent) => {
    setActiveAlarmEvent(event);
    voiceAgent.speak(getEffectiveVoiceMessage(event), event.voicePersona);
  };

  const snoozeAlarm = (event: AgendaEvent) => {
    dismissAlarm();
    // Schedule reminder in 10 minutes (or 10 seconds in demo)
    window.setTimeout(() => {
      triggerAlarmModal(event);
    }, 600000);
  };

  const dismissAlarm = () => {
    voiceAgent.stop();
    setActiveAlarmEvent(null);
  };

  const handleQuickPrompt = (prompt: string) => {
    if (prompt === '¿Qué tengo hoy?') {
      const text =
        todayEvents.length === 0
          ? 'Para hoy no tienes eventos agendados. ¡Tienes tiempo libre para ti!'
          : `Hoy tienes ${todayEvents.length} evento(s): ${todayEvents
              .map((e) => e.title)
              .join(', ')}`;
      voiceAgent.speak(text, 'Cálida');
    } else if (prompt === '¿Cuál es mi próximo evento?') {
      if (upcomingEvents.length === 0) {
        voiceAgent.speak(
          'No tienes próximos eventos en tu agenda. Puedes crear uno nuevo pulsando el botón más.',
          'Motivadora'
        );
      } else {
        const next = upcomingEvents[0];
        const dateStr = new Intl.DateTimeFormat('es-ES', {
          weekday: 'long',
          day: 'numeric',
          month: 'long',
        }).format(new Date(next.dateTimeEpochMs));
        voiceAgent.speak(
          `Tu próximo evento es ${next.title}, programado para el ${dateStr}.`,
          'Motivadora'
        );
      }
    } else if (prompt === '¿Cápsula de recuerdos?') {
      setSelectedTab(1);
      voiceAgent.speak(
        `Bienvenido a tu cápsula. Cuentas con ${memories.length} recuerdo(s) guardados. Revive cada dibujo y nota cuando lo desees.`,
        'Poética'
      );
    } else if (prompt === 'Dame un consejo del día') {
      const tips = [
        'Cada día es un lienzo en blanco. Recuerda que no se trata solo de cumplir fechas, sino de disfrutar los momentos.',
        'La mejor forma de alcanzar grandes metas es celebrar cada pequeño avance. ¡Sigue adelante!',
        'Un minuto de organización al despertar te regala una hora de paz durante el día. ¡Aura está contigo!',
      ];
      const randomTip = tips[Math.floor(Math.random() * tips.length)];
      voiceAgent.speak(randomTip, 'Motivadora');
    }
  };

  return (
    <div className="min-h-screen bg-[#0D0C1D] text-[#F1F5F9] flex flex-col">
      {/* Top App Bar */}
      <header className="sticky top-0 z-30 bg-[#16152B]/95 backdrop-blur-md border-b border-slate-800/80 px-4 h-14 flex items-center justify-between">
        <div className="w-24" />

        {/* Center Brand Title */}
        <div className="flex items-center gap-2">
          <div className="w-8 h-8 rounded-full bg-indigo-900 flex items-center justify-center">
            <AudioWaveform className="w-5 h-5 text-indigo-400" />
          </div>
          <h1 className="text-lg font-extrabold tracking-tight text-slate-100">VoxAgenda</h1>
        </div>

        {/* Actions */}
        <div className="flex items-center justify-end gap-1.5 w-auto">
          <button
            type="button"
            data-testid="topbar_qr_btn"
            onClick={() => setShowQrDialog(true)}
            title="Compartir con Código QR"
            className="p-2 rounded-full text-indigo-400 hover:bg-indigo-500/15 transition-colors"
          >
            <QrCode className="w-5 h-5" />
          </button>

          <button
            type="button"
            data-testid="topbar_vip_btn"
            onClick={() => setShowVipDialog(true)}
            title={isVipActive ? 'Aura VIP Activo' : 'Aura VIP Club'}
            className="p-1.5 rounded-full hover:bg-indigo-500/15 transition-colors"
          >
            {isVipActive ? (
              <Award className="w-5 h-5 text-amber-400" />
            ) : (
              <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-lg bg-indigo-500/15 border border-indigo-500/50 text-[11px] font-extrabold text-indigo-400">
                <Award className="w-3.5 h-3.5" />
                VIP
              </span>
            )}
          </button>

          <button
            type="button"
            data-testid="topbar_market_btn"
            onClick={() => setShowMarketDialog(true)}
            title="Mercado y Negocio"
            className="p-2 rounded-full text-indigo-400 hover:bg-indigo-500/15 transition-colors"
          >
            <Lightbulb className="w-5 h-5" />
          </button>
        </div>
      </header>

      {/* Main Content Container */}
      <main className="flex-1 w-full max-w-2xl mx-auto p-4">
        {selectedTab === 0 && (
          <div data-testid="agenda_list" className="space-y-3.5 pb-28">
            {/* Companion Hero Briefing Card */}
            <AgentBriefingCard
              todayEvents={todayEvents}
              upcomingEvents={upcomingEvents}
              isSpeaking={isSpeaking}
              currentSpokenText={currentSpokenText}
              onPlayBriefing={playDailyBriefing}
              onStopSpeaking={stopSpeaking}
              onOpenMarketAnalysis={() => setShowMarketDialog(true)}
              onQuickPrompt={handleQuickPrompt}
            />

            {/* Calendar Day Strip */}
            <CalendarStrip
              selectedDayEpoch={selectedDayEpoch}
              onSelectDay={(epoch) => setSelectedDayEpoch(epoch)}
              events={allEvents}
            />

            {/* Category Filter Chips */}
            <div className="flex items-center gap-2 overflow-x-auto no-scrollbar py-0.5">
              {filterOptions.map((cat) => {
                const isSelected = selectedCategoryFilter === cat;
                return (
                  <button
                    key={cat}
                    type="button"
                    data-testid={`filter_${cat}`}
                    onClick={() => setSelectedCategoryFilter(cat)}
                    className={`px-3.5 py-1.5 rounded-lg text-xs font-semibold whitespace-nowrap shrink-0 transition-colors ${
                      isSelected
                        ? 'bg-indigo-400 text-slate-950 font-bold'
                        : 'bg-[#16152B] border border-slate-700/80 text-slate-300 hover:bg-[#222040]'
                    }`}
                  >
                    {cat}
                  </button>
                );
              })}
            </div>

            {/* Section Title */}
            <h2 className="text-base font-bold text-slate-100 pt-1">
              {selectedDayEpoch !== null
                ? `Eventos del día seleccionado (${filteredEvents.length})`
                : `Próximos Eventos y Fechas (${filteredEvents.length})`}
            </h2>

            {/* Events List or Empty State */}
            {filteredEvents.length === 0 ? (
              <div className="rounded-2xl bg-[#222040]/40 border border-slate-800 p-6 flex flex-col items-center text-center">
                <Calendar className="w-10 h-10 text-indigo-400 mb-2.5" />
                <h3 className="text-sm font-bold text-slate-100">
                  No hay eventos con estos filtros
                </h3>
                <p className="text-xs text-slate-400 mt-1 max-w-xs">
                  Pulsa &lsquo;+ Nuevo Evento / Dibujo&rsquo; para crear una fecha importante con su
                  dibujo y voz.
                </p>
              </div>
            ) : (
              <div className="space-y-3.5">
                {filteredEvents.map((event) => (
                  <EventCard
                    key={event.id}
                    event={event}
                    onSpeakVoice={() => speakEvent(event)}
                    onTriggerTestAlarm={() => triggerAlarmModal(event)}
                    onToggleCompleted={() => toggleCompleted(event)}
                    onEdit={() => openEditDialog(event)}
                    onDelete={() => deleteEvent(event)}
                    onSendGift={() => {
                      setGiftTargetEvent(event);
                      setShowGiftDialog(true);
                    }}
                  />
                ))}
              </div>
            )}

            {/* Bottom branding */}
            <div className="pt-4 pb-2 flex flex-col items-center text-center">
              <span className="text-xs font-bold text-indigo-400">🦁 Lyon Studio</span>
              <span className="text-[11px] text-slate-400/80 mt-0.5">
                Creando experiencias digitales con alma
              </span>
            </div>
          </div>
        )}

        {selectedTab === 1 && (
          <MemoryCapsuleView
            memories={memories}
            onSpeakMemory={speakMemory}
            onAddNewMemory={openAddDialog}
            onOpenMemoryBook={() => setShowMemoryBookDialog(true)}
          />
        )}

        {selectedTab === 2 && (
          <AgentCompanionView
            allEvents={allEvents}
            memories={memories}
            isSpeaking={isSpeaking}
            onTestVoice={previewVoice}
            onOpenMarketAnalysis={() => setShowMarketDialog(true)}
          />
        )}
      </main>

      {/* Floating Action Button */}
      {selectedTab !== 2 && (
        <div className="fixed bottom-20 right-4 md:right-[calc(50%-20rem)] z-30">
          <button
            type="button"
            data-testid="fab_add_event"
            onClick={openAddDialog}
            className="px-5 py-3.5 rounded-2xl bg-indigo-400 hover:bg-indigo-300 text-slate-950 font-bold text-sm shadow-xl shadow-indigo-950/60 flex items-center gap-2 transition-transform active:scale-95"
          >
            <Plus className="w-5 h-5 stroke-[2.5]" />
            <span>Nuevo Evento / Dibujo</span>
          </button>
        </div>
      )}

      {/* Bottom Navigation Bar */}
      <nav
        data-testid="bottom_nav_bar"
        className="fixed bottom-0 left-0 right-0 z-30 bg-[#16152B]/95 backdrop-blur-md border-t border-slate-800 h-16"
      >
        <div className="max-w-2xl mx-auto h-full grid grid-cols-3">
          <button
            type="button"
            data-testid="tab_agenda"
            onClick={() => setSelectedTab(0)}
            className="flex flex-col items-center justify-center gap-1 transition-colors"
          >
            <div
              className={`px-4 py-1 rounded-full transition-colors ${
                selectedTab === 0
                  ? 'bg-indigo-900 text-indigo-200'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <CalendarCheck2 className="w-5 h-5" />
            </div>
            <span
              className={`text-[11px] font-semibold ${
                selectedTab === 0 ? 'text-indigo-300' : 'text-slate-400'
              }`}
            >
              Agenda
            </span>
          </button>

          <button
            type="button"
            data-testid="tab_memories"
            onClick={() => setSelectedTab(1)}
            className="flex flex-col items-center justify-center gap-1 transition-colors"
          >
            <div
              className={`px-4 py-1 rounded-full transition-colors ${
                selectedTab === 1
                  ? 'bg-indigo-900 text-indigo-200'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Heart className="w-5 h-5" />
            </div>
            <span
              className={`text-[11px] font-semibold ${
                selectedTab === 1 ? 'text-indigo-300' : 'text-slate-400'
              }`}
            >
              Recuerdos
            </span>
          </button>

          <button
            type="button"
            data-testid="tab_agent"
            onClick={() => setSelectedTab(2)}
            className="flex flex-col items-center justify-center gap-1 transition-colors"
          >
            <div
              className={`px-4 py-1 rounded-full transition-colors ${
                selectedTab === 2
                  ? 'bg-indigo-900 text-indigo-200'
                  : 'text-slate-400 hover:text-slate-200'
              }`}
            >
              <Brain className="w-5 h-5" />
            </div>
            <span
              className={`text-[11px] font-semibold ${
                selectedTab === 2 ? 'text-indigo-300' : 'text-slate-400'
              }`}
            >
              Agente Aura
            </span>
          </button>
        </div>
      </nav>

      {/* Modals & Dialogs */}
      {showAddEditDialog && (
        <AddEditEventDialog
          initialEvent={editingEvent}
          onSave={saveEvent}
          onDismiss={closeAddEditDialog}
          onPreviewVoice={previewVoice}
        />
      )}

      {activeAlarmEvent && (
        <VoiceAlarmDialog
          event={activeAlarmEvent}
          isSpeaking={isSpeaking}
          onSpeakAgain={() => speakEvent(activeAlarmEvent)}
          onSnooze={() => snoozeAlarm(activeAlarmEvent)}
          onDismiss={dismissAlarm}
        />
      )}

      {showMarketDialog && (
        <MarketInnovationDialog onDismiss={() => setShowMarketDialog(false)} />
      )}

      {showVipDialog && (
        <AuraVipDialog
          isVipActive={isVipActive}
          onToggleVip={(active) => setIsVipActive(active)}
          onDismiss={() => setShowVipDialog(false)}
        />
      )}

      {showGiftDialog && (
        <GiftAffiliateDialog
          targetEvent={giftTargetEvent}
          onDismiss={() => {
            setShowGiftDialog(false);
            setGiftTargetEvent(null);
          }}
        />
      )}

      {showMemoryBookDialog && (
        <MemoryBookDialog
          memories={memories}
          onDismiss={() => setShowMemoryBookDialog(false)}
        />
      )}

      {showQrDialog && <ShareQrDialog onDismiss={() => setShowQrDialog(false)} />}
    </div>
  );
}

export default App;
