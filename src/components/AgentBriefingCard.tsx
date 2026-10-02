import React from 'react';
import { Info, Mic, StopCircle, Sparkles } from 'lucide-react';
import { AgendaEvent } from '../types/agenda';

interface AgentBriefingCardProps {
  todayEvents: AgendaEvent[];
  upcomingEvents: AgendaEvent[];
  isSpeaking: boolean;
  currentSpokenText: string;
  onPlayBriefing: () => void;
  onStopSpeaking: () => void;
  onOpenMarketAnalysis: () => void;
  onQuickPrompt: (prompt: string) => void;
}

export const AgentBriefingCard: React.FC<AgentBriefingCardProps> = ({
  todayEvents,
  isSpeaking,
  currentSpokenText,
  onPlayBriefing,
  onStopSpeaking,
  onOpenMarketAnalysis,
  onQuickPrompt,
}) => {
  const hour = new Date().getHours();
  const greeting =
    hour >= 5 && hour <= 11
      ? '¡Buenos días!'
      : hour >= 12 && hour <= 18
      ? '¡Buenas tardes!'
      : '¡Buenas noches!';

  const quickPrompts = [
    '¿Qué tengo hoy?',
    '¿Cuál es mi próximo evento?',
    '¿Cápsula de recuerdos?',
    'Dame un consejo del día',
  ];

  return (
    <div
      data-testid="agent_briefing_card"
      className="relative w-full rounded-3xl bg-[#222040]/70 border border-indigo-500/20 p-5 shadow-md overflow-hidden"
    >
      {/* Radial ambient glow */}
      <div
        className="pointer-events-none absolute -top-16 -left-16 w-72 h-72 rounded-full opacity-25 blur-3xl"
        style={{
          background: 'radial-gradient(circle, rgba(129,140,248,0.6) 0%, transparent 70%)',
        }}
      />

      <div className="relative z-10 flex items-center gap-3.5">
        {/* Agent Avatar with speaking pulse */}
        <div className="relative w-16 h-16 flex items-center justify-center shrink-0">
          {isSpeaking && (
            <div className="absolute inset-0 rounded-full bg-indigo-500/40 animate-ping" />
          )}
          <img
            src="/assets/agent_avatar_aura.png"
            alt="Avatar de Aura"
            referrerPolicy="no-referrer"
            className="relative w-14 h-14 rounded-full object-cover border-2 border-indigo-400 shadow-sm"
          />
        </div>

        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-1.5">
            <span className="text-xs font-bold text-indigo-400">Aura • Agente Personal</span>
            <span
              className={`w-2 h-2 rounded-full ${
                isSpeaking ? 'bg-emerald-400 animate-pulse' : 'bg-indigo-500'
              }`}
            />
          </div>
          <h2 className="text-xl font-extrabold text-slate-100 truncate">{greeting}</h2>
          <p className="text-xs text-slate-300">
            {todayEvents.length > 0
              ? `Tienes ${todayEvents.length} evento(s) para hoy`
              : 'Tu día está tranquilo y despejado'}
          </p>
        </div>

        <button
          type="button"
          data-testid="btn_market_info"
          onClick={onOpenMarketAnalysis}
          title="Estudio de Mercado e Innovación"
          className="p-2.5 rounded-full text-indigo-400 hover:bg-indigo-500/15 transition-colors shrink-0"
        >
          <Info className="w-5 h-5" />
        </button>
      </div>

      {/* Spoken text indicator when speaking */}
      {isSpeaking && currentSpokenText && (
        <div className="relative z-10 mt-3 rounded-xl bg-indigo-900/50 border border-indigo-500/30 p-3 flex items-start gap-2.5">
          <Mic className="w-4 h-4 text-indigo-400 shrink-0 mt-0.5 animate-pulse" />
          <p className="text-xs text-indigo-100 line-clamp-3 leading-relaxed">
            {currentSpokenText}
          </p>
        </div>
      )}

      {/* Action button: Daily Voice Briefing */}
      <div className="relative z-10 mt-4">
        {isSpeaking ? (
          <button
            type="button"
            data-testid="btn_stop_speaking"
            onClick={onStopSpeaking}
            className="w-full py-2.5 px-4 rounded-full bg-rose-600 hover:bg-rose-500 text-white text-sm font-semibold flex items-center justify-center gap-2 transition-colors shadow-sm"
          >
            <StopCircle className="w-4 h-4" />
            <span>Silenciar Voz</span>
          </button>
        ) : (
          <button
            type="button"
            data-testid="btn_play_briefing"
            onClick={onPlayBriefing}
            className="w-full py-2.5 px-4 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-sm flex items-center justify-center gap-2 transition-colors shadow-sm"
          >
            <Mic className="w-4 h-4" />
            <span>🎙️ Resumen por Voz</span>
          </button>
        )}
      </div>

      {/* Interactive Agent dialogue chips */}
      <div className="relative z-10 mt-3 flex items-center gap-2 overflow-x-auto no-scrollbar pb-1">
        {quickPrompts.map((prompt) => (
          <button
            key={prompt}
            type="button"
            onClick={() => onQuickPrompt(prompt)}
            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full bg-[#16152B]/85 border border-slate-700/80 hover:border-indigo-400/60 text-xs text-slate-200 whitespace-nowrap shrink-0 transition-colors"
          >
            <Sparkles className="w-3 h-3 text-indigo-400" />
            <span>{prompt}</span>
          </button>
        ))}
      </div>
    </div>
  );
};
