import React, { useState } from 'react';
import { Camera, Sparkles, X } from 'lucide-react';
import { AgendaEvent, parseDoodle } from '../types/agenda';
import { DoodleThumbnail } from './DoodleCanvas';

interface EventVisualThumbnailProps {
  event: AgendaEvent;
  className?: string;
  enableZoomOnClick?: boolean;
}

export const EventVisualThumbnail: React.FC<EventVisualThumbnailProps> = ({
  event,
  className = 'w-[68px] h-[56px]',
  enableZoomOnClick = true,
}) => {
  const [showFullViewDialog, setShowFullViewDialog] = useState(false);

  const doodle = parseDoodle(event.doodleJson);

  const getAiArtUrl = () => {
    if (
      event.aiArtStyle.toLowerCase().includes('paisaje') ||
      event.aiArtStyle.toLowerCase().includes('scenic') ||
      event.category.toLowerCase().includes('viaje')
    ) {
      return '/assets/ai_art_scenic.png';
    }
    return '/assets/ai_art_celebration.png';
  };

  return (
    <>
      <div
        onClick={enableZoomOnClick ? () => setShowFullViewDialog(true) : undefined}
        className={`relative rounded-xl overflow-hidden border border-slate-700/60 shrink-0 ${
          enableZoomOnClick ? 'cursor-pointer hover:ring-2 hover:ring-indigo-400 transition-all' : ''
        } ${className}`}
      >
        {event.visualType === 'PHOTO' ? (
          <>
            {event.photoUri ? (
              <img
                src={event.photoUri}
                alt="Foto del evento"
                referrerPolicy="no-referrer"
                className="w-full h-full object-cover"
              />
            ) : (
              <div className="w-full h-full bg-slate-800 flex items-center justify-center">
                <Camera className="w-6 h-6 text-indigo-400" />
              </div>
            )}
            <div className="absolute bottom-1 right-1 w-4 h-4 rounded-full bg-black/65 flex items-center justify-center">
              <Camera className="w-2.5 h-2.5 text-white" />
            </div>
          </>
        ) : event.visualType === 'AI_ART' ? (
          <>
            <img
              src={getAiArtUrl()}
              alt="Arte IA del evento"
              referrerPolicy="no-referrer"
              className="w-full h-full object-cover"
            />
            <div className="absolute bottom-1 right-1 w-4 h-4 rounded-full bg-indigo-500/85 flex items-center justify-center">
              <Sparkles className="w-2.5 h-2.5 text-white" />
            </div>
          </>
        ) : (
          <DoodleThumbnail drawing={doodle} className="w-full h-full" />
        )}
      </div>

      {/* High-Resolution Dialog Viewer on Click */}
      {showFullViewDialog && enableZoomOnClick && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center bg-black/70 backdrop-blur-sm p-4"
          onClick={() => setShowFullViewDialog(false)}
        >
          <div
            className="w-full max-w-md rounded-3xl bg-[#16152B] border border-slate-700/70 p-5 shadow-2xl"
            onClick={(e) => e.stopPropagation()}
          >
            <div className="flex items-center justify-between mb-4">
              <div>
                <h3 className="text-base font-bold text-slate-100">{event.title}</h3>
                <p className="text-xs text-indigo-400">
                  {event.visualType === 'PHOTO'
                    ? '📷 Fotografía del evento'
                    : event.visualType === 'AI_ART'
                    ? '✨ Ilustración artística por IA'
                    : '🎨 Dibujo original hecho a mano'}
                </p>
              </div>
              <button
                type="button"
                onClick={() => setShowFullViewDialog(false)}
                className="p-2 rounded-full text-slate-400 hover:text-white hover:bg-slate-800 transition-colors"
              >
                <X className="w-5 h-5" />
              </button>
            </div>

            <div className="w-full h-72 rounded-2xl overflow-hidden border border-slate-700/70 bg-[#0D0C1D]">
              {event.visualType === 'PHOTO' ? (
                <img
                  src={event.photoUri}
                  alt="Foto completa"
                  referrerPolicy="no-referrer"
                  className="w-full h-full object-cover"
                />
              ) : event.visualType === 'AI_ART' ? (
                <img
                  src={getAiArtUrl()}
                  alt="Arte IA"
                  referrerPolicy="no-referrer"
                  className="w-full h-full object-cover"
                />
              ) : (
                <DoodleThumbnail drawing={doodle} className="w-full h-full" />
              )}
            </div>

            {event.visualType === 'AI_ART' && (
              <p className="mt-3 text-center text-xs font-medium text-pink-400">
                Estilo: {event.aiArtStyle || 'Celebración & Acuarela'}
              </p>
            )}
          </div>
        </div>
      )}
    </>
  );
};
