import React from 'react';
import { AudioWaveform, CheckCircle2, Clock, RotateCcw } from 'lucide-react';
import { AgendaEvent, getEffectiveVoiceMessage, parseDoodle } from '../types/agenda';
import { EventVisualThumbnail } from './EventVisualDisplay';

interface VoiceAlarmDialogProps {
  event: AgendaEvent;
  isSpeaking: boolean;
  onSpeakAgain: () => void;
  onSnooze: () => void;
  onDismiss: () => void;
}

export const VoiceAlarmDialog: React.FC<VoiceAlarmDialogProps> = ({
  event,
  isSpeaking,
  onSpeakAgain,
  onSnooze,
  onDismiss,
}) => {
  const doodle = parseDoodle(event.doodleJson);
  const hasDoodle = doodle.strokes.length > 0 || doodle.stamps.length > 0;
  const hasVisual =
    (event.visualType === 'PHOTO' && event.photoUri.trim().length > 0) ||
    event.visualType === 'AI_ART' ||
    (event.visualType === 'DOODLE' && hasDoodle);

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/75 backdrop-blur-sm p-4">
      <div
        data-testid="voice_alarm_dialog"
        className="w-full max-w-md rounded-[28px] bg-gradient-to-b from-indigo-950/90 to-[#16152B] border border-indigo-500/40 p-6 shadow-2xl flex flex-col items-center text-center"
      >
        {/* Header tag */}
        <div className="inline-flex items-center gap-1.5 px-3.5 py-1.5 rounded-full bg-indigo-500/20 text-indigo-300 text-xs font-bold mb-3">
          <AudioWaveform className="w-4 h-4 animate-pulse" />
          <span>ALARMA DE VOZ ACTIVA</span>
        </div>

        {/* Avatar with glowing ring */}
        <div className="relative w-28 h-28 flex items-center justify-center">
          {isSpeaking && (
            <div className="absolute inset-1 rounded-full bg-indigo-500/30 animate-ping" />
          )}
          <img
            src="/assets/agent_avatar_aura.png"
            alt="Avatar de Aura"
            referrerPolicy="no-referrer"
            className="relative w-[86px] h-[86px] rounded-full object-cover border-[3px] border-indigo-400 shadow-lg"
          />
        </div>

        <h2 className="mt-3.5 text-xl font-extrabold text-slate-100">{event.title}</h2>
        <p className="text-xs text-slate-400 mt-0.5">
          Categoría: {event.category} • Voz: {event.voicePersona}
        </p>

        {/* Visual preview */}
        {hasVisual && (
          <div className="mt-4 flex flex-col items-center">
            <span className="text-xs text-indigo-400 mb-1.5">
              {event.visualType === 'PHOTO'
                ? 'Foto del evento:'
                : event.visualType === 'AI_ART'
                ? 'Arte ilustrado del evento:'
                : 'Tu dibujo del evento:'}
            </span>
            <EventVisualThumbnail
              event={event}
              className="w-[130px] h-[84px]"
              enableZoomOnClick={false}
            />
          </div>
        )}

        {/* Speech card bubble */}
        <div className="mt-4 w-full rounded-[18px] bg-[#222040]/75 border border-slate-700/70 p-4 text-left">
          <div className="flex items-center justify-between mb-1.5">
            <span className="text-xs font-bold text-indigo-400">🗣️ Mensaje del Agente:</span>
            {isSpeaking && (
              <span className="text-[11px] text-emerald-400 font-medium animate-pulse">
                Hablando...
              </span>
            )}
          </div>
          <p className="text-sm text-slate-200 leading-relaxed">
            &ldquo;{getEffectiveVoiceMessage(event)}&rdquo;
          </p>
        </div>

        {/* Actions */}
        <div className="mt-5 w-full flex items-center gap-2">
          <button
            type="button"
            data-testid="alarm_btn_replay"
            onClick={onSpeakAgain}
            className="flex-1 py-2.5 px-3 rounded-full border border-slate-700 hover:bg-slate-800 text-xs font-semibold text-slate-200 flex items-center justify-center gap-1.5 transition-colors"
          >
            <RotateCcw className="w-4 h-4" />
            <span>Repetir</span>
          </button>

          <button
            type="button"
            data-testid="alarm_btn_snooze"
            onClick={onSnooze}
            className="flex-1 py-2.5 px-3 rounded-full bg-indigo-900/70 hover:bg-indigo-800 text-xs font-semibold text-indigo-100 flex items-center justify-center gap-1.5 transition-colors"
          >
            <Clock className="w-4 h-4" />
            <span>Posponer</span>
          </button>
        </div>

        <button
          type="button"
          data-testid="alarm_btn_dismiss"
          onClick={onDismiss}
          className="mt-2.5 w-full py-3 px-4 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-sm flex items-center justify-center gap-2 transition-colors shadow-md"
        >
          <CheckCircle2 className="w-5 h-5" />
          <span>¡Enterado! Apagar Alarma</span>
        </button>
      </div>
    </div>
  );
};
