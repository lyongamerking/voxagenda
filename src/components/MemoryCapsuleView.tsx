import React from 'react';
import { ArrowRight, BookOpen, Heart, Plus, Volume2 } from 'lucide-react';
import { AgendaEvent, parseDoodle } from '../types/agenda';
import { EventVisualThumbnail } from './EventVisualDisplay';

interface MemoryCapsuleViewProps {
  memories: AgendaEvent[];
  onSpeakMemory: (memory: AgendaEvent) => void;
  onAddNewMemory: () => void;
  onOpenMemoryBook: () => void;
}

export const MemoryCapsuleView: React.FC<MemoryCapsuleViewProps> = ({
  memories,
  onSpeakMemory,
  onAddNewMemory,
  onOpenMemoryBook,
}) => {
  const formatDate = (ms: number) =>
    new Intl.DateTimeFormat('es-ES', {
      day: 'numeric',
      month: 'long',
      year: 'numeric',
    }).format(new Date(ms));

  return (
    <div data-testid="memory_capsule_view" className="w-full space-y-3.5 pb-24">
      {/* Banner */}
      <div className="w-full rounded-[20px] bg-pink-950/45 border border-pink-500/25 p-4">
        <div className="flex items-center gap-2">
          <Heart className="w-6 h-6 text-pink-400 fill-pink-400" />
          <h2 className="text-base font-extrabold text-pink-400">Cápsula de Recuerdos</h2>
        </div>
        <p className="mt-1 text-xs text-slate-300 leading-relaxed">
          Tu agente personal preserva tus vivencias más hermosas, dibujos y reflexiones para que
          nunca los olvides.
        </p>
      </div>

      {/* Print-On-Demand Memory Book Banner */}
      <div
        data-testid="btn_open_memory_book_banner"
        onClick={onOpenMemoryBook}
        className="w-full rounded-2xl bg-[#16152B] border-[1.5px] border-indigo-500/50 p-3.5 flex items-center gap-3 cursor-pointer hover:bg-[#1E1C38] transition-colors"
      >
        <div className="w-11 h-11 rounded-xl bg-indigo-500/15 flex items-center justify-center shrink-0">
          <BookOpen className="w-6 h-6 text-indigo-400" />
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-1.5">
            <span className="text-sm font-bold text-indigo-300">📖 Imprimir Libro Físico</span>
            <span className="px-1.5 py-0.5 rounded bg-emerald-500 text-white text-[10px] font-bold">
              NUEVO
            </span>
          </div>
          <p className="text-xs text-slate-400 mt-0.5">
            Convierte tus fotos, notas y dibujos en un libro de pasta dura.
          </p>
        </div>
        <ArrowRight className="w-5 h-5 text-indigo-400 shrink-0" />
      </div>

      {/* Memories List or Empty State */}
      {memories.length === 0 ? (
        <div className="w-full py-10 px-6 text-center flex flex-col items-center">
          <h3 className="text-sm font-semibold text-slate-300">
            ✨ Aún no tienes recuerdos guardados
          </h3>
          <p className="mt-1.5 text-xs text-slate-400 max-w-sm">
            Al crear un evento, activa &lsquo;Guardar en Cápsula de Recuerdos&rsquo; o añade uno
            ahora mismo.
          </p>
          <button
            type="button"
            data-testid="btn_add_first_memory"
            onClick={onAddNewMemory}
            className="mt-4 inline-flex items-center gap-1.5 px-4 py-2.5 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-xs transition-colors"
          >
            <Plus className="w-4 h-4" />
            <span>Crear Primer Recuerdo</span>
          </button>
        </div>
      ) : (
        <div className="space-y-3.5">
          {memories.map((memory) => {
            const doodle = parseDoodle(memory.doodleJson);
            const hasDoodle = doodle.strokes.length > 0 || doodle.stamps.length > 0;
            const hasVisual =
              (memory.visualType === 'PHOTO' && memory.photoUri.trim().length > 0) ||
              memory.visualType === 'AI_ART' ||
              (memory.visualType === 'DOODLE' && hasDoodle);

            return (
              <div
                key={memory.id}
                data-testid={`memory_card_${memory.id}`}
                className="w-full rounded-[18px] bg-[#16152B] border border-slate-800 p-4 shadow-sm"
              >
                <div className="flex items-center gap-2">
                  <span className="px-2.5 py-1 rounded-full bg-pink-900/70 text-pink-200 text-xs font-bold">
                    {memory.category}
                  </span>
                  <span className="text-xs text-slate-400">
                    {formatDate(memory.dateTimeEpochMs)}
                  </span>
                </div>

                <h3 className="mt-2 text-base font-bold text-slate-100">{memory.title}</h3>

                {memory.description && (
                  <p className="mt-1 text-xs text-slate-300">{memory.description}</p>
                )}

                {memory.reflectionNote && (
                  <div className="mt-2.5 rounded-xl bg-pink-500/10 p-2.5">
                    <p className="text-xs text-pink-300">
                      💌 Nota del corazón: &ldquo;{memory.reflectionNote}&rdquo;
                    </p>
                  </div>
                )}

                {hasVisual && (
                  <div className="mt-3">
                    <p className="text-xs text-indigo-400 mb-1.5">
                      {memory.visualType === 'PHOTO'
                        ? 'Foto guardada con el recuerdo:'
                        : memory.visualType === 'AI_ART'
                        ? 'Arte ilustrado con el recuerdo:'
                        : 'Boceto guardado con el recuerdo:'}
                    </p>
                    <EventVisualThumbnail event={memory} className="w-[140px] h-[80px]" />
                  </div>
                )}

                <button
                  type="button"
                  data-testid={`btn_narrate_memory_${memory.id}`}
                  onClick={() => onSpeakMemory(memory)}
                  className="mt-3 w-full py-2.5 px-4 rounded-full bg-indigo-900/70 hover:bg-indigo-800 text-indigo-100 text-xs font-semibold flex items-center justify-center gap-2 transition-colors"
                >
                  <Volume2 className="w-4 h-4" />
                  <span>🎙️ Escuchar Narración del Agente</span>
                </button>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};
