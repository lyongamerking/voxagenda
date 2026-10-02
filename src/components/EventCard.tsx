import React from 'react';
import {
  AlarmClock,
  BellRing,
  CheckCircle2,
  Circle,
  Edit2,
  Gift,
  Heart,
  Trash2,
  Volume2,
} from 'lucide-react';
import { AgendaEvent, parseDoodle } from '../types/agenda';
import { EventVisualThumbnail } from './EventVisualDisplay';

interface EventCardProps {
  event: AgendaEvent;
  onSpeakVoice: () => void;
  onTriggerTestAlarm: () => void;
  onToggleCompleted: () => void;
  onEdit: () => void;
  onDelete: () => void;
  onSendGift?: () => void;
}

export const EventCard: React.FC<EventCardProps> = ({
  event,
  onSpeakVoice,
  onTriggerTestAlarm,
  onToggleCompleted,
  onEdit,
  onDelete,
  onSendGift,
}) => {
  const formattedDateTime = new Intl.DateTimeFormat('es-ES', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  }).format(new Date(event.dateTimeEpochMs));

  const doodle = parseDoodle(event.doodleJson);
  const hasDoodle = doodle.strokes.length > 0 || doodle.stamps.length > 0;
  const hasVisual =
    (event.visualType === 'PHOTO' && event.photoUri.trim().length > 0) ||
    event.visualType === 'AI_ART' ||
    (event.visualType === 'DOODLE' && hasDoodle);

  const importanceStyles =
    event.importance === 'Prioritaria'
      ? 'bg-rose-500/15 text-rose-400'
      : event.importance === 'Alta'
      ? 'bg-amber-500/15 text-amber-400'
      : 'bg-indigo-500/15 text-indigo-400';

  const isGiftCategory = [
    'Cumpleaños',
    'Aniversario',
    'Especial',
    'Familia',
    'Recuerdo',
  ].includes(event.category);

  return (
    <div
      data-testid={`event_card_${event.id}`}
      className={`w-full rounded-[20px] p-4 border transition-all ${
        event.isCompleted
          ? 'bg-[#222040]/40 border-slate-800/70'
          : 'bg-[#16152B] border-slate-800 shadow-md'
      }`}
    >
      {/* Header Row: Category, Importance, Memory badge, Actions */}
      <div className="flex items-center gap-1.5 flex-wrap">
        <span className="px-2.5 py-1 rounded-full bg-indigo-900/70 text-indigo-100 text-xs font-bold">
          {event.category}
        </span>

        <span className={`px-2.5 py-1 rounded-full text-xs font-semibold ${importanceStyles}`}>
          {event.importance}
        </span>

        {event.isMemoryCapsule && (
          <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-full bg-pink-900/70 text-pink-200 text-xs font-medium">
            <Heart className="w-3 h-3 text-pink-400 fill-pink-400" />
            Recuerdo
          </span>
        )}

        <div className="flex-1" />

        <button
          type="button"
          data-testid={`btn_edit_${event.id}`}
          onClick={onEdit}
          title="Editar"
          className="p-1.5 rounded-lg text-indigo-400 hover:bg-indigo-500/15 transition-colors"
        >
          <Edit2 className="w-4 h-4" />
        </button>
        <button
          type="button"
          data-testid={`btn_delete_${event.id}`}
          onClick={onDelete}
          title="Eliminar"
          className="p-1.5 rounded-lg text-rose-400 hover:bg-rose-500/15 transition-colors"
        >
          <Trash2 className="w-4 h-4" />
        </button>
      </div>

      {/* Main Content: Checkbox, Title & Time, and miniature Doodle Thumbnail */}
      <div className="mt-3 flex items-start gap-2.5">
        <button
          type="button"
          data-testid={`toggle_complete_${event.id}`}
          onClick={onToggleCompleted}
          title="Completar"
          className="mt-0.5 p-1 rounded-full hover:bg-slate-800 transition-colors shrink-0"
        >
          {event.isCompleted ? (
            <CheckCircle2 className="w-5 h-5 text-emerald-400" />
          ) : (
            <Circle className="w-5 h-5 text-slate-500" />
          )}
        </button>

        <div className="flex-1 min-w-0">
          <h3
            className={`text-base font-bold leading-snug ${
              event.isCompleted ? 'line-through text-slate-400' : 'text-slate-100'
            }`}
          >
            {event.title}
          </h3>
          <p className="text-xs text-slate-400 mt-0.5">{formattedDateTime}</p>

          {event.description && (
            <p className="text-xs text-slate-200/80 mt-1 line-clamp-2">{event.description}</p>
          )}

          {event.reflectionNote && (
            <p className="text-xs text-pink-400 mt-1 line-clamp-2">
              💌 Nota: &ldquo;{event.reflectionNote}&rdquo;
            </p>
          )}
        </div>

        {hasVisual && (
          <EventVisualThumbnail event={event} className="w-[68px] h-[56px]" />
        )}
      </div>

      {/* Voice info banner */}
      {event.isAlarmEnabled && (
        <div className="mt-3 px-3 py-1.5 rounded-xl bg-indigo-500/10 flex items-center gap-2">
          <AlarmClock className="w-4 h-4 text-indigo-400 shrink-0" />
          <span className="text-xs text-indigo-300 font-medium">
            Voz: {event.voicePersona} • Alarma programada
          </span>
        </div>
      )}

      {/* Affiliate Gift Suggestion Banner for Special Dates */}
      {isGiftCategory && onSendGift && (
        <div
          data-testid={`gift_banner_${event.id}`}
          onClick={onSendGift}
          className="mt-2.5 px-3 py-2 rounded-xl bg-amber-50 border border-amber-200 flex items-center gap-2.5 cursor-pointer hover:bg-amber-100/90 transition-colors"
        >
          <span className="text-base">💐</span>
          <div className="flex-1 min-w-0">
            <p className="text-xs font-bold text-amber-900">
              Enviar flores o regalo (Cupón AURA10)
            </p>
            <p className="text-[11px] text-amber-700">
              Sugerencia con entrega a domicilio programada
            </p>
          </div>
          <Gift className="w-4 h-4 text-amber-600 shrink-0" />
        </div>
      )}

      {/* Bottom Buttons: Speak preview & Trigger Live Alarm Now */}
      <div className="mt-3 flex items-center gap-2">
        <button
          type="button"
          data-testid={`btn_speak_${event.id}`}
          onClick={onSpeakVoice}
          className="flex-1 py-2 px-3 rounded-full border border-slate-700 hover:border-indigo-400/60 hover:bg-slate-800/60 text-xs font-semibold text-indigo-300 flex items-center justify-center gap-1.5 transition-colors"
        >
          <Volume2 className="w-4 h-4" />
          <span>Escuchar Voz</span>
        </button>

        <button
          type="button"
          data-testid={`btn_trigger_alarm_${event.id}`}
          onClick={onTriggerTestAlarm}
          className="flex-1 py-2 px-3 rounded-full bg-indigo-900/70 hover:bg-indigo-800 text-xs font-semibold text-indigo-100 flex items-center justify-center gap-1.5 transition-colors"
        >
          <BellRing className="w-4 h-4" />
          <span>Probar Alarma</span>
        </button>
      </div>
    </div>
  );
};
