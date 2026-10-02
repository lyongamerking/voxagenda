import React from 'react';
import { Info, Lightbulb, Mic, Volume2 } from 'lucide-react';
import { AgendaEvent } from '../types/agenda';

interface AgentCompanionViewProps {
  allEvents: AgendaEvent[];
  memories: AgendaEvent[];
  isSpeaking: boolean;
  onTestVoice: (text: string, persona: string) => void;
  onOpenMarketAnalysis: () => void;
}

export const AgentCompanionView: React.FC<AgentCompanionViewProps> = ({
  allEvents,
  memories,
  isSpeaking,
  onTestVoice,
  onOpenMarketAnalysis,
}) => {
  const completedCount = allEvents.filter((e) => e.isCompleted).length;
  const pendingCount = allEvents.filter((e) => !e.isCompleted).length;

  const voiceSamples = [
    {
      persona: 'Cálida',
      sampleText: '¡Hola! Estoy aquí para acompañarte y recordarte tus días más bonitos.',
    },
    {
      persona: 'Motivadora',
      sampleText: '¡A levantarse con todo el ánimo! Hoy alcanzaremos grandes metas.',
    },
    {
      persona: 'Ejecutiva',
      sampleText: 'Atención: Recordatorio puntual de compromisos y objetivos clave.',
    },
    {
      persona: 'Poética',
      sampleText: 'Los momentos más especiales se graban en el alma para siempre.',
    },
  ];

  return (
    <div className="w-full space-y-4 pb-24">
      {/* Hero Avatar Card */}
      <div className="w-full rounded-3xl bg-indigo-900/35 border border-indigo-500/25 p-5 flex items-center gap-4">
        <img
          src="/assets/agent_avatar_aura.png"
          alt="Avatar de Aura"
          referrerPolicy="no-referrer"
          className="w-[76px] h-[76px] rounded-full object-cover border-[3px] border-indigo-400 shrink-0"
        />
        <div>
          <h2 className="text-xl font-extrabold text-slate-100">Aura</h2>
          <p className="text-xs text-indigo-400 font-medium">
            Tu Agente Personal de Vida &amp; Recuerdos
          </p>
          <div className="mt-1.5 inline-block">
            <span
              className={`px-2.5 py-1 rounded-full text-xs font-medium ${
                isSpeaking
                  ? 'bg-emerald-500/20 text-emerald-300'
                  : 'bg-[#222040] text-slate-300'
              }`}
            >
              {isSpeaking ? '🎙️ Hablando ahora...' : '✨ Conectada y lista para recordarte'}
            </span>
          </div>
        </div>
      </div>

      {/* Stats Row */}
      <div className="grid grid-cols-3 gap-2.5">
        <div className="rounded-2xl bg-[#222040]/50 border border-slate-800 p-3.5 flex flex-col items-center">
          <span className="text-2xl font-bold text-indigo-400 tabular-nums">{pendingCount}</span>
          <span className="text-xs text-slate-400 mt-0.5">Por cumplir</span>
        </div>
        <div className="rounded-2xl bg-[#222040]/50 border border-slate-800 p-3.5 flex flex-col items-center">
          <span className="text-2xl font-bold text-pink-400 tabular-nums">{memories.length}</span>
          <span className="text-xs text-slate-400 mt-0.5">Recuerdos</span>
        </div>
        <div className="rounded-2xl bg-[#222040]/50 border border-slate-800 p-3.5 flex flex-col items-center">
          <span className="text-2xl font-bold text-emerald-400 tabular-nums">{completedCount}</span>
          <span className="text-xs text-slate-400 mt-0.5">Completados</span>
        </div>
      </div>

      {/* Voice Personalities Sandbox */}
      <div className="rounded-[20px] bg-[#222040]/40 border border-slate-800 p-4 space-y-3">
        <div>
          <div className="flex items-center gap-2">
            <Mic className="w-5 h-5 text-indigo-400" />
            <h3 className="text-base font-bold text-slate-100">Voces del Agente</h3>
          </div>
          <p className="text-xs text-slate-400 mt-0.5">
            Prueba cómo suena cada tono para tus alarmas:
          </p>
        </div>

        <div className="space-y-2">
          {voiceSamples.map(({ persona, sampleText }) => (
            <div
              key={persona}
              className="rounded-xl bg-[#16152B] border border-slate-700/70 px-3.5 py-2.5 flex items-center gap-3"
            >
              <div className="flex-1 min-w-0">
                <p className="text-sm font-bold text-slate-100">{persona}</p>
                <p className="text-xs text-slate-400 mt-0.5">{sampleText}</p>
              </div>
              <button
                type="button"
                data-testid={`test_voice_${persona}`}
                onClick={() => onTestVoice(sampleText, persona)}
                title="Escuchar"
                className="p-2 rounded-full text-indigo-400 hover:bg-indigo-500/15 transition-colors shrink-0"
              >
                <Volume2 className="w-5 h-5" />
              </button>
            </div>
          ))}
        </div>
      </div>

      {/* Market Analysis Button */}
      <div className="rounded-[20px] bg-cyan-950/30 border border-cyan-500/25 p-4">
        <div className="flex items-center gap-2">
          <Lightbulb className="w-5 h-5 text-sky-400" />
          <h3 className="text-base font-bold text-slate-100">
            ¿Existe algo parecido en el mercado?
          </h3>
        </div>
        <p className="mt-1 text-xs text-slate-300 leading-relaxed">
          Aprende cómo VoxAgenda supera a Google Calendar, TimeTree y Notion con dibujos, alarmas
          habladas y memoria emocional.
        </p>
        <button
          type="button"
          data-testid="btn_open_market_agent_tab"
          onClick={onOpenMarketAnalysis}
          className="mt-3 w-full py-2.5 px-4 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-xs flex items-center justify-center gap-2 transition-colors"
        >
          <Info className="w-4 h-4" />
          <span>Ver Estudio de Mercado e Innovación</span>
        </button>
      </div>
    </div>
  );
};
