import React, { useRef, useState } from 'react';
import {
  Calendar,
  Clock,
  Heart,
  ImagePlus,
  Mic,
  Sparkles,
  Volume2,
  X,
} from 'lucide-react';
import {
  AgendaEvent,
  AiArtStylesList,
  CategoriesList,
  DoodleDrawing,
  ImportanceLevels,
  Personas,
  VisualType,
  getEffectiveVoiceMessage,
  parseDoodle,
  serializeDoodle,
} from '../types/agenda';
import { InteractiveDoodlePad } from './DoodleCanvas';

interface AddEditEventDialogProps {
  initialEvent: AgendaEvent | null;
  onSave: (event: AgendaEvent) => void;
  onDismiss: () => void;
  onPreviewVoice: (text: string, persona: string) => void;
}

export const AddEditEventDialog: React.FC<AddEditEventDialogProps> = ({
  initialEvent,
  onSave,
  onDismiss,
  onPreviewVoice,
}) => {
  const [title, setTitle] = useState(initialEvent?.title || '');
  const [description, setDescription] = useState(initialEvent?.description || '');
  const [category, setCategory] = useState(initialEvent?.category || CategoriesList[0]);
  const [importance, setImportance] = useState(initialEvent?.importance || 'Alta');
  const [visualType, setVisualType] = useState<VisualType>(initialEvent?.visualType || 'DOODLE');
  const [photoUriString, setPhotoUriString] = useState(initialEvent?.photoUri || '');
  const [aiArtStyle, setAiArtStyle] = useState(
    initialEvent?.aiArtStyle || 'Celebración & Acuarela'
  );
  const [voicePersona, setVoicePersona] = useState(initialEvent?.voicePersona || 'Cálida');
  const [customVoiceMessage, setCustomVoiceMessage] = useState(
    initialEvent?.voiceMessageCustom || ''
  );
  const [isAlarmEnabled, setIsAlarmEnabled] = useState(
    initialEvent ? initialEvent.isAlarmEnabled : true
  );
  const [isMemoryCapsule, setIsMemoryCapsule] = useState(
    initialEvent ? initialEvent.isMemoryCapsule : false
  );
  const [reflectionNote, setReflectionNote] = useState(initialEvent?.reflectionNote || '');

  const initialEpoch = initialEvent?.dateTimeEpochMs || Date.now() + 3600000;
  const initialDateObj = new Date(initialEpoch);

  const [dateStr, setDateStr] = useState(() => {
    const y = initialDateObj.getFullYear();
    const m = String(initialDateObj.getMonth() + 1).padStart(2, '0');
    const d = String(initialDateObj.getDate()).padStart(2, '0');
    return `${y}-${m}-${d}`;
  });

  const [timeStr, setTimeStr] = useState(() => {
    const h = String(initialDateObj.getHours()).padStart(2, '0');
    const min = String(initialDateObj.getMinutes()).padStart(2, '0');
    return `${h}:${min}`;
  });

  const [doodleDrawing, setDoodleDrawing] = useState<DoodleDrawing>(() =>
    parseDoodle(initialEvent?.doodleJson)
  );

  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const computeEpochMs = (): number => {
    const [y, m, d] = dateStr.split('-').map(Number);
    const [hr, mn] = timeStr.split(':').map(Number);
    const dt = new Date(
      y || initialDateObj.getFullYear(),
      (m || 1) - 1,
      d || 1,
      hr || 0,
      mn || 0,
      0,
      0
    );
    return dt.getTime();
  };

  const handlePhotoSelect = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = () => {
      if (typeof reader.result === 'string') {
        setPhotoUriString(reader.result);
        setVisualType('PHOTO');
      }
    };
    reader.readAsDataURL(file);
  };

  const handleSave = () => {
    if (!title.trim()) return;
    const eventToSave: AgendaEvent = {
      id: initialEvent?.id || 0,
      title: title.trim(),
      description: description.trim(),
      dateTimeEpochMs: computeEpochMs(),
      category,
      importance,
      visualType,
      doodleJson: serializeDoodle(doodleDrawing),
      photoUri: photoUriString,
      aiArtStyle,
      voiceMessageCustom: customVoiceMessage.trim(),
      voicePersona,
      isAlarmEnabled,
      isCompleted: initialEvent?.isCompleted || false,
      isMemoryCapsule,
      reflectionNote: reflectionNote.trim(),
      creationTimestamp: initialEvent?.creationTimestamp || Date.now(),
    };
    onSave(eventToSave);
  };

  const currentAiPreviewUrl = aiArtStyle.toLowerCase().includes('paisaje')
    ? '/assets/ai_art_scenic.png'
    : '/assets/ai_art_celebration.png';

  return (
    <div className="fixed inset-0 z-50 bg-[#0D0C1D] flex flex-col overflow-hidden">
      {/* Top Bar */}
      <header className="flex items-center justify-between px-4 py-3 bg-[#16152B] border-b border-slate-800 shrink-0">
        <div className="flex items-center gap-2">
          <button
            type="button"
            data-testid="dialog_close_btn"
            onClick={onDismiss}
            className="p-2 rounded-full text-slate-300 hover:bg-slate-800 transition-colors"
          >
            <X className="w-5 h-5" />
          </button>
          <h2 className="text-base font-bold text-slate-100">
            {initialEvent === null ? 'Nuevo Evento o Recuerdo' : 'Editar Evento'}
          </h2>
        </div>

        <button
          type="button"
          data-testid="dialog_save_btn"
          disabled={!title.trim()}
          onClick={handleSave}
          className="px-4 py-2 rounded-full bg-indigo-500 hover:bg-indigo-400 disabled:opacity-40 disabled:pointer-events-none text-slate-950 font-bold text-xs transition-colors"
        >
          Guardar
        </button>
      </header>

      {/* Scrollable Content */}
      <div className="flex-1 overflow-y-auto p-4 max-w-2xl w-full mx-auto space-y-4 pb-16">
        {/* Title */}
        <div>
          <label className="block text-xs font-semibold text-indigo-300 mb-1">
            Nombre del Evento o Recuerdo *
          </label>
          <input
            type="text"
            data-testid="event_title_input"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Ej: Cumpleaños de mamá, Viaje a la playa, Aniversario..."
            className="w-full px-3.5 py-2.5 rounded-xl bg-[#16152B] border border-slate-700 text-sm text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-indigo-400"
          />
        </div>

        {/* Category & Importance */}
        <div className="space-y-2">
          <label className="block text-xs font-semibold text-indigo-400">Categoría:</label>
          <div className="flex items-center gap-2 overflow-x-auto no-scrollbar pb-1">
            {CategoriesList.map((cat) => (
              <button
                key={cat}
                type="button"
                data-testid={`cat_chip_${cat}`}
                onClick={() => setCategory(cat)}
                className={`px-3 py-1.5 rounded-lg text-xs font-semibold whitespace-nowrap shrink-0 transition-colors ${
                  category === cat
                    ? 'bg-indigo-500 text-slate-950'
                    : 'bg-[#16152B] border border-slate-700 text-slate-300 hover:bg-slate-800'
                }`}
              >
                {cat}
              </button>
            ))}
          </div>
        </div>

        <div className="space-y-2">
          <label className="block text-xs font-semibold text-indigo-400">Importancia:</label>
          <div className="flex items-center gap-2">
            {ImportanceLevels.map((lvl) => (
              <button
                key={lvl}
                type="button"
                onClick={() => setImportance(lvl)}
                className={`px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors ${
                  importance === lvl
                    ? 'bg-indigo-500 text-slate-950'
                    : 'bg-[#16152B] border border-slate-700 text-slate-300 hover:bg-slate-800'
                }`}
              >
                {lvl}
              </button>
            ))}
          </div>
        </div>

        {/* Date & Time pickers */}
        <div className="rounded-2xl bg-[#222040]/60 border border-slate-800 p-3.5 space-y-2.5">
          <h3 className="text-xs font-bold text-slate-200">Fecha y Hora del Evento</h3>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-2.5">
            <label className="flex items-center gap-2 px-3 py-2 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-200">
              <Calendar className="w-4 h-4 text-indigo-400 shrink-0" />
              <input
                type="date"
                data-testid="pick_date_btn"
                value={dateStr}
                onChange={(e) => setDateStr(e.target.value)}
                className="bg-transparent w-full focus:outline-none text-slate-100"
              />
            </label>
            <label className="flex items-center gap-2 px-3 py-2 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-200">
              <Clock className="w-4 h-4 text-indigo-400 shrink-0" />
              <input
                type="time"
                data-testid="pick_time_btn"
                value={timeStr}
                onChange={(e) => setTimeStr(e.target.value)}
                className="bg-transparent w-full focus:outline-none text-slate-100"
              />
            </label>
          </div>
        </div>

        {/* 3 VISUAL OPTIONS: DOODLE / PHOTO / AI ART */}
        <div className="rounded-[20px] bg-[#222040]/50 border border-slate-800 p-4 space-y-3">
          <div>
            <h3 className="text-base font-bold text-indigo-400">🖼️ Imagen o Arte del Evento</h3>
            <p className="text-xs text-slate-400">
              Elige cómo quieres ilustrar este momento especial:
            </p>
          </div>

          <div className="grid grid-cols-3 gap-1.5">
            <button
              type="button"
              data-testid="tab_visual_doodle"
              onClick={() => setVisualType('DOODLE')}
              className={`py-2 px-2 rounded-xl text-xs font-semibold transition-colors whitespace-nowrap truncate ${
                visualType === 'DOODLE'
                  ? 'bg-indigo-500 text-slate-950 font-bold'
                  : 'bg-[#16152B] text-slate-300 border border-slate-700'
              }`}
            >
              🎨 Dibujo a Mano
            </button>
            <button
              type="button"
              data-testid="tab_visual_photo"
              onClick={() => setVisualType('PHOTO')}
              className={`py-2 px-2 rounded-xl text-xs font-semibold transition-colors whitespace-nowrap truncate ${
                visualType === 'PHOTO'
                  ? 'bg-indigo-500 text-slate-950 font-bold'
                  : 'bg-[#16152B] text-slate-300 border border-slate-700'
              }`}
            >
              📷 Foto Real
            </button>
            <button
              type="button"
              data-testid="tab_visual_ai"
              onClick={() => setVisualType('AI_ART')}
              className={`py-2 px-2 rounded-xl text-xs font-semibold transition-colors whitespace-nowrap truncate ${
                visualType === 'AI_ART'
                  ? 'bg-indigo-500 text-slate-950 font-bold'
                  : 'bg-[#16152B] text-slate-300 border border-slate-700'
              }`}
            >
              ✨ Boceto IA
            </button>
          </div>

          {visualType === 'DOODLE' && (
            <div className="space-y-2">
              <p className="text-xs text-slate-300">
                Dibuja libremente con tu dedo o cursor, o añade sellos:
              </p>
              <InteractiveDoodlePad
                initialDrawing={doodleDrawing}
                onDrawingChanged={(updated) => setDoodleDrawing(updated)}
              />
            </div>
          )}

          {visualType === 'PHOTO' && (
            <div className="flex flex-col items-center space-y-3">
              <input
                ref={fileInputRef}
                type="file"
                accept="image/*"
                onChange={handlePhotoSelect}
                className="hidden"
              />
              {photoUriString ? (
                <>
                  <div className="w-full h-48 rounded-2xl overflow-hidden border-2 border-indigo-400">
                    <img
                      src={photoUriString}
                      alt="Foto seleccionada"
                      referrerPolicy="no-referrer"
                      className="w-full h-full object-cover"
                    />
                  </div>
                  <button
                    type="button"
                    data-testid="btn_change_photo"
                    onClick={() => fileInputRef.current?.click()}
                    className="inline-flex items-center gap-2 px-4 py-2 rounded-full bg-indigo-900/70 hover:bg-indigo-800 text-indigo-100 text-xs font-semibold transition-colors"
                  >
                    <ImagePlus className="w-4 h-4" />
                    <span>Cambiar Foto de la Galería</span>
                  </button>
                </>
              ) : (
                <div
                  data-testid="btn_pick_photo_placeholder"
                  onClick={() => fileInputRef.current?.click()}
                  className="w-full py-7 px-4 rounded-2xl bg-[#16152B] border border-slate-700 hover:border-indigo-400 cursor-pointer flex flex-col items-center text-center transition-colors"
                >
                  <ImagePlus className="w-10 h-10 text-indigo-400 mb-2" />
                  <p className="text-sm font-bold text-slate-100">
                    Toca para elegir una foto de tu galería
                  </p>
                  <p className="text-xs text-slate-400 mt-0.5">
                    Foto de mamá, aniversario, viaje, amigos...
                  </p>
                </div>
              )}
            </div>
          )}

          {visualType === 'AI_ART' && (
            <div className="space-y-3">
              <p className="text-xs text-slate-300">
                Elige un estilo artístico generado para este evento:
              </p>
              <div className="grid grid-cols-2 gap-3">
                {AiArtStylesList.map((style) => {
                  const isSelected = aiArtStyle === style.name;
                  return (
                    <div
                      key={style.name}
                      data-testid={`ai_style_${style.name}`}
                      onClick={() => setAiArtStyle(style.name)}
                      className={`rounded-2xl overflow-hidden cursor-pointer border transition-all ${
                        isSelected
                          ? 'border-2 border-indigo-400 bg-indigo-950/40'
                          : 'border-slate-700 bg-[#16152B]'
                      }`}
                    >
                      <img
                        src={style.imageUrl}
                        alt={style.name}
                        referrerPolicy="no-referrer"
                        className="w-full h-28 object-cover"
                      />
                      <p
                        className={`p-2 text-xs ${
                          isSelected ? 'font-bold text-indigo-300' : 'text-slate-300'
                        }`}
                      >
                        {style.name}
                      </p>
                    </div>
                  );
                })}
              </div>

              <div className="relative w-full h-48 rounded-2xl overflow-hidden border-2 border-indigo-400">
                <img
                  src={currentAiPreviewUrl}
                  alt="Vista previa arte IA"
                  referrerPolicy="no-referrer"
                  className="w-full h-full object-cover"
                />
                <div className="absolute bottom-0 right-0 rounded-tl-xl bg-black/75 px-2.5 py-1.5 flex items-center gap-1.5">
                  <Sparkles className="w-3.5 h-3.5 text-amber-300" />
                  <span className="text-[11px] text-white font-medium">
                    Vista previa en alta calidad
                  </span>
                </div>
              </div>

              <div className="rounded-xl bg-indigo-500/10 p-2.5 flex items-center gap-2">
                <Sparkles className="w-4 h-4 text-indigo-400 shrink-0" />
                <p className="text-xs text-indigo-300">
                  Estilo: {aiArtStyle}. Se adaptará e ilustrará &lsquo;
                  {title.trim() ? title : 'tu evento'}&rsquo;.
                </p>
              </div>
            </div>
          )}
        </div>

        {/* Voice Alarm & Personal Agent Configuration */}
        <div className="rounded-[18px] bg-indigo-950/35 border border-indigo-500/25 p-4 space-y-3">
          <div className="flex items-center justify-between gap-3">
            <div className="flex items-center gap-2.5">
              <Mic className="w-5 h-5 text-indigo-400 shrink-0" />
              <div>
                <h3 className="text-sm font-bold text-slate-100">Alarma con Voz de Agente</h3>
                <p className="text-xs text-slate-400">
                  El agente te hablará en voz alta cuando llegue la fecha
                </p>
              </div>
            </div>
            <input
              type="checkbox"
              data-testid="alarm_switch"
              checked={isAlarmEnabled}
              onChange={(e) => setIsAlarmEnabled(e.target.checked)}
              className="w-5 h-5 accent-indigo-500 rounded cursor-pointer"
            />
          </div>

          {isAlarmEnabled && (
            <div className="space-y-3 pt-2 border-t border-indigo-500/20">
              <div>
                <p className="text-xs font-semibold text-slate-200 mb-2">
                  Personalidad de la Voz:
                </p>
                <div className="grid grid-cols-1 sm:grid-cols-2 gap-1.5">
                  {Personas.map(({ key, label }) => (
                    <button
                      key={key}
                      type="button"
                      data-testid={`persona_${key}`}
                      onClick={() => setVoicePersona(key)}
                      className={`px-3 py-2 rounded-xl text-xs font-semibold text-left transition-colors ${
                        voicePersona === key
                          ? 'bg-indigo-500 text-slate-950 font-bold'
                          : 'bg-[#16152B] border border-slate-700 text-slate-300 hover:bg-slate-800'
                      }`}
                    >
                      {label}
                    </button>
                  ))}
                </div>
              </div>

              <div>
                <label className="block text-xs font-medium text-slate-300 mb-1">
                  Mensaje de voz personalizado (opcional)
                </label>
                <textarea
                  rows={2}
                  data-testid="custom_voice_input"
                  value={customVoiceMessage}
                  onChange={(e) => setCustomVoiceMessage(e.target.value)}
                  placeholder="Si lo dejas vacío, el agente creará un mensaje automático."
                  className="w-full px-3 py-2 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-indigo-400"
                />
              </div>

              <button
                type="button"
                data-testid="preview_voice_btn"
                onClick={() => {
                  const previewMsg = getEffectiveVoiceMessage({
                    title: title.trim() || 'Mi Evento',
                    importance,
                    voiceMessageCustom: customVoiceMessage,
                    voicePersona,
                  });
                  onPreviewVoice(previewMsg, voicePersona);
                }}
                className="w-full py-2.5 px-4 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-xs flex items-center justify-center gap-2 transition-colors"
              >
                <Volume2 className="w-4 h-4" />
                <span>🔊 Probar cómo sonará la voz</span>
              </button>
            </div>
          )}
        </div>

        {/* Memory Capsule toggle */}
        <div className="rounded-[18px] bg-pink-950/30 border border-pink-500/25 p-4 space-y-3">
          <div className="flex items-center justify-between gap-3">
            <div className="flex items-center gap-2.5">
              <Heart className="w-5 h-5 text-pink-400 fill-pink-400 shrink-0" />
              <div>
                <h3 className="text-sm font-bold text-slate-100">
                  Guardar en Cápsula de Recuerdos
                </h3>
                <p className="text-xs text-slate-400">
                  Preserva este evento en el cofre de memorias nostálgicas
                </p>
              </div>
            </div>
            <input
              type="checkbox"
              data-testid="capsule_switch"
              checked={isMemoryCapsule}
              onChange={(e) => setIsMemoryCapsule(e.target.checked)}
              className="w-5 h-5 accent-pink-500 rounded cursor-pointer"
            />
          </div>

          {isMemoryCapsule && (
            <div className="pt-2 border-t border-pink-500/20">
              <label className="block text-xs font-medium text-pink-200 mb-1">
                Nota de recuerdo o reflexión emocional
              </label>
              <textarea
                rows={2}
                data-testid="reflection_note_input"
                value={reflectionNote}
                onChange={(e) => setReflectionNote(e.target.value)}
                placeholder="¿Qué hace único a este recuerdo? ¿Cómo te sentiste?"
                className="w-full px-3 py-2 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-pink-400"
              />
            </div>
          )}
        </div>

        {/* Description */}
        <div>
          <label className="block text-xs font-semibold text-slate-300 mb-1">
            Detalles o notas adicionales
          </label>
          <textarea
            rows={2}
            data-testid="event_desc_input"
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Cosas por hacer, personas involucradas, ubicación..."
            className="w-full px-3.5 py-2.5 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-indigo-400"
          />
        </div>
      </div>
    </div>
  );
};
