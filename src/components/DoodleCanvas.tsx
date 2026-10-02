import React, { useEffect, useRef, useState } from 'react';
import { Brush, Sparkles, Eraser, Undo2, Trash2 } from 'lucide-react';
import {
  DoodleColors,
  DoodleDrawing,
  DoodlePoint,
  DoodleStamp,
  DoodleStamps,
  DoodleStroke,
  createSampleBeach,
  createSampleCake,
  createSampleGraduation,
} from '../types/agenda';

interface DoodleThumbnailProps {
  drawing: DoodleDrawing;
  className?: string;
}

export const DoodleThumbnail: React.FC<DoodleThumbnailProps> = ({
  drawing,
  className = 'w-full h-full',
}) => {
  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const width = canvas.width;
    const height = canvas.height;

    ctx.clearRect(0, 0, width, height);
    ctx.fillStyle = drawing.bgColorHex || '#1E1B4B';
    ctx.fillRect(0, 0, width, height);

    const scaleX = width / 200;
    const scaleY = height / 200;
    const scale = Math.min(scaleX, scaleY);

    for (const stroke of drawing.strokes) {
      if (!stroke.points || stroke.points.length < 2) continue;
      ctx.beginPath();
      ctx.strokeStyle = stroke.colorHex || '#FFFFFF';
      ctx.lineWidth = Math.max(stroke.strokeWidth * scale, 1.5);
      ctx.lineCap = 'round';
      ctx.lineJoin = 'round';
      ctx.moveTo(stroke.points[0].x * scaleX, stroke.points[0].y * scaleY);
      for (let i = 1; i < stroke.points.length; i++) {
        ctx.lineTo(stroke.points[i].x * scaleX, stroke.points[i].y * scaleY);
      }
      ctx.stroke();
    }

    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    for (const stamp of drawing.stamps) {
      const fontSize = Math.max(stamp.size * scale, 12);
      ctx.font = `${fontSize}px sans-serif`;
      ctx.fillText(stamp.symbol, stamp.x * scaleX, stamp.y * scaleY);
    }
  }, [drawing]);

  return (
    <div
      className={`overflow-hidden rounded-xl flex items-center justify-center ${className}`}
      style={{ backgroundColor: drawing.bgColorHex || '#1E1B4B' }}
    >
      <canvas ref={canvasRef} width={300} height={240} className="w-full h-full block" />
    </div>
  );
};

interface InteractiveDoodlePadProps {
  initialDrawing: DoodleDrawing;
  onDrawingChanged: (drawing: DoodleDrawing) => void;
}

export const InteractiveDoodlePad: React.FC<InteractiveDoodlePadProps> = ({
  initialDrawing,
  onDrawingChanged,
}) => {
  const [strokes, setStrokes] = useState<DoodleStroke[]>(initialDrawing.strokes || []);
  const [stamps, setStamps] = useState<DoodleStamp[]>(initialDrawing.stamps || []);
  const [selectedColorHex, setSelectedColorHex] = useState<string>(DoodleColors[0]);
  const [selectedStrokeWidth, setSelectedStrokeWidth] = useState<number>(8);
  const [selectedTool, setSelectedTool] = useState<'pen' | 'stamp' | 'eraser'>('pen');
  const [selectedStamp, setSelectedStamp] = useState<string>(DoodleStamps[0]);
  const [currentPoints, setCurrentPoints] = useState<DoodlePoint[]>([]);
  const [isDrawing, setIsDrawing] = useState<boolean>(false);

  const canvasRef = useRef<HTMLCanvasElement | null>(null);

  const emitChange = (nextStrokes: DoodleStroke[], nextStamps: DoodleStamp[]) => {
    onDrawingChanged({
      strokes: nextStrokes,
      stamps: nextStamps,
      bgColorHex: '#1E1B4B',
    });
  };

  // Render canvas whenever strokes, stamps, or currentPoints change
  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    ctx.clearRect(0, 0, canvas.width, canvas.height);
    ctx.fillStyle = '#16152B';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    const drawStroke = (pts: DoodlePoint[], color: string, width: number) => {
      if (pts.length < 2) return;
      ctx.beginPath();
      ctx.strokeStyle = color;
      ctx.lineWidth = width;
      ctx.lineCap = 'round';
      ctx.lineJoin = 'round';
      ctx.moveTo(pts[0].x, pts[0].y);
      for (let i = 1; i < pts.length; i++) {
        ctx.lineTo(pts[i].x, pts[i].y);
      }
      ctx.stroke();
    };

    for (const s of strokes) {
      drawStroke(s.points, s.colorHex, s.strokeWidth);
    }

    if (currentPoints.length >= 2) {
      drawStroke(currentPoints, selectedColorHex, selectedStrokeWidth);
    }

    ctx.textAlign = 'center';
    ctx.textBaseline = 'middle';
    for (const st of stamps) {
      ctx.font = `${st.size}px sans-serif`;
      ctx.fillText(st.symbol, st.x, st.y);
    }
  }, [strokes, stamps, currentPoints, selectedColorHex, selectedStrokeWidth]);

  // Convert pointer coordinates into normalized 200x200 coordinate space
  const getCanvasPoint = (e: React.PointerEvent<HTMLCanvasElement>): DoodlePoint => {
    const canvas = canvasRef.current;
    if (!canvas) return { x: 0, y: 0 };
    const rect = canvas.getBoundingClientRect();
    const scaleX = canvas.width / rect.width;
    const scaleY = canvas.height / rect.height;
    return {
      x: (e.clientX - rect.left) * scaleX,
      y: (e.clientY - rect.top) * scaleY,
    };
  };

  const handlePointerDown = (e: React.PointerEvent<HTMLCanvasElement>) => {
    e.currentTarget.setPointerCapture(e.pointerId);
    const pt = getCanvasPoint(e);

    if (selectedTool === 'pen') {
      setIsDrawing(true);
      setCurrentPoints([pt, { x: pt.x + 0.5, y: pt.y + 0.5 }]);
    } else if (selectedTool === 'stamp') {
      const nextStamps = [
        ...stamps,
        {
          x: pt.x,
          y: pt.y,
          symbol: selectedStamp,
          size: 28,
        },
      ];
      setStamps(nextStamps);
      emitChange(strokes, nextStamps);
    } else if (selectedTool === 'eraser') {
      // Remove nearest stroke or stamp
      const strokeIdx = [...strokes]
        .reverse()
        .findIndex((s) =>
          s.points.some((p) => (p.x - pt.x) * (p.x - pt.x) + (p.y - pt.y) * (p.y - pt.y) < 400)
        );
      if (strokeIdx >= 0) {
        const actualIdx = strokes.length - 1 - strokeIdx;
        const nextStrokes = strokes.filter((_, idx) => idx !== actualIdx);
        setStrokes(nextStrokes);
        emitChange(nextStrokes, stamps);
        return;
      }
      const stampIdx = [...stamps]
        .reverse()
        .findIndex((st) => (st.x - pt.x) * (st.x - pt.x) + (st.y - pt.y) * (st.y - pt.y) < 400);
      if (stampIdx >= 0) {
        const actualIdx = stamps.length - 1 - stampIdx;
        const nextStamps = stamps.filter((_, idx) => idx !== actualIdx);
        setStamps(nextStamps);
        emitChange(strokes, nextStamps);
      }
    }
  };

  const handlePointerMove = (e: React.PointerEvent<HTMLCanvasElement>) => {
    if (!isDrawing || selectedTool !== 'pen') return;
    const pt = getCanvasPoint(e);
    setCurrentPoints((prev) => [...prev, pt]);
  };

  const handlePointerUp = () => {
    if (!isDrawing) return;
    setIsDrawing(false);
    if (selectedTool === 'pen' && currentPoints.length > 0) {
      const nextStrokes = [
        ...strokes,
        {
          points: currentPoints,
          colorHex: selectedColorHex,
          strokeWidth: selectedStrokeWidth,
        },
      ];
      setStrokes(nextStrokes);
      setCurrentPoints([]);
      emitChange(nextStrokes, stamps);
    }
  };

  const handleUndo = () => {
    if (strokes.length > 0) {
      const nextStrokes = strokes.slice(0, -1);
      setStrokes(nextStrokes);
      emitChange(nextStrokes, stamps);
    } else if (stamps.length > 0) {
      const nextStamps = stamps.slice(0, -1);
      setStamps(nextStamps);
      emitChange(strokes, nextStamps);
    }
  };

  const handleClear = () => {
    setStrokes([]);
    setStamps([]);
    setCurrentPoints([]);
    emitChange([], []);
  };

  const applyPreset = (preset: DoodleDrawing) => {
    setStrokes(preset.strokes);
    setStamps(preset.stamps);
    emitChange(preset.strokes, preset.stamps);
  };

  return (
    <div className="w-full rounded-2xl bg-slate-800/40 border border-slate-700/50 p-3 space-y-2.5">
      {/* Toolbar: Tools + Undo + Clear */}
      <div className="flex flex-wrap items-center justify-between gap-2">
        <div className="flex items-center gap-1.5">
          <button
            type="button"
            data-testid="tool_pen"
            onClick={() => setSelectedTool('pen')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors whitespace-nowrap ${
              selectedTool === 'pen'
                ? 'bg-indigo-600 text-white shadow-sm'
                : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
            }`}
          >
            <Brush className="w-3.5 h-3.5" />
            Pincel
          </button>
          <button
            type="button"
            data-testid="tool_stamp"
            onClick={() => setSelectedTool('stamp')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors whitespace-nowrap ${
              selectedTool === 'stamp'
                ? 'bg-indigo-600 text-white shadow-sm'
                : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
            }`}
          >
            <Sparkles className="w-3.5 h-3.5" />
            Stickers
          </button>
          <button
            type="button"
            data-testid="tool_eraser"
            onClick={() => setSelectedTool('eraser')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-lg text-xs font-semibold transition-colors whitespace-nowrap ${
              selectedTool === 'eraser'
                ? 'bg-indigo-600 text-white shadow-sm'
                : 'bg-slate-800 text-slate-300 hover:bg-slate-700'
            }`}
          >
            <Eraser className="w-3.5 h-3.5" />
            Borrador
          </button>
        </div>

        <div className="flex items-center gap-1">
          <button
            type="button"
            data-testid="doodle_undo"
            onClick={handleUndo}
            title="Deshacer"
            className="p-2 rounded-lg text-slate-300 hover:bg-slate-700/70 transition-colors"
          >
            <Undo2 className="w-4 h-4" />
          </button>
          <button
            type="button"
            data-testid="doodle_clear"
            onClick={handleClear}
            title="Borrar todo"
            className="p-2 rounded-lg text-rose-400 hover:bg-rose-500/15 transition-colors"
          >
            <Trash2 className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Subtoolbar */}
      {selectedTool === 'pen' && (
        <div className="flex flex-wrap items-center justify-between gap-2 pt-1">
          <div className="flex items-center gap-2 overflow-x-auto no-scrollbar py-0.5">
            {DoodleColors.map((hex) => {
              const isSelected = selectedColorHex === hex;
              return (
                <button
                  key={hex}
                  type="button"
                  data-testid={`color_picker_${hex}`}
                  onClick={() => setSelectedColorHex(hex)}
                  className={`w-7 h-7 rounded-full shrink-0 transition-transform ${
                    isSelected
                      ? 'ring-2 ring-indigo-400 scale-110'
                      : 'ring-1 ring-slate-600 hover:scale-105'
                  }`}
                  style={{ backgroundColor: hex }}
                  aria-label={`Color ${hex}`}
                />
              );
            })}
          </div>

          <div className="flex items-center gap-1">
            {[
              { width: 4, label: 'Fino' },
              { width: 8, label: 'Medio' },
              { width: 14, label: 'Grueso' },
            ].map(({ width, label }) => {
              const isSelected = selectedStrokeWidth === width;
              return (
                <button
                  key={label}
                  type="button"
                  onClick={() => setSelectedStrokeWidth(width)}
                  className={`px-2.5 py-1 rounded-md text-xs font-medium border transition-colors whitespace-nowrap ${
                    isSelected
                      ? 'bg-indigo-500/25 border-indigo-400 text-indigo-200'
                      : 'border-slate-700 text-slate-400 hover:text-slate-200'
                  }`}
                >
                  {label}
                </button>
              );
            })}
          </div>
        </div>
      )}

      {selectedTool === 'stamp' && (
        <div className="flex items-center gap-2 overflow-x-auto no-scrollbar py-1">
          {DoodleStamps.map((emoji) => {
            const isSelected = selectedStamp === emoji;
            return (
              <button
                key={emoji}
                type="button"
                data-testid={`stamp_${emoji}`}
                onClick={() => setSelectedStamp(emoji)}
                className={`px-3 py-1.5 rounded-xl text-xl shrink-0 transition-all ${
                  isSelected
                    ? 'bg-indigo-600/40 ring-2 ring-indigo-400 scale-105'
                    : 'bg-slate-800/80 hover:bg-slate-700'
                }`}
              >
                {emoji}
              </button>
            );
          })}
        </div>
      )}

      {/* Quick Presets */}
      <div className="flex items-center gap-2 text-xs text-slate-400">
        <span>Plantillas rápidas:</span>
        <button
          type="button"
          onClick={() => applyPreset(createSampleCake())}
          className="px-2 py-0.5 rounded bg-slate-800 hover:bg-slate-700 text-slate-200 transition-colors"
        >
          🎂 Pastel
        </button>
        <button
          type="button"
          onClick={() => applyPreset(createSampleBeach())}
          className="px-2 py-0.5 rounded bg-slate-800 hover:bg-slate-700 text-slate-200 transition-colors"
        >
          🏖️ Playa
        </button>
        <button
          type="button"
          onClick={() => applyPreset(createSampleGraduation())}
          className="px-2 py-0.5 rounded bg-slate-800 hover:bg-slate-700 text-slate-200 transition-colors"
        >
          🏆 Logro
        </button>
      </div>

      {/* Canvas Drawing Area */}
      <div
        data-testid="doodle_canvas_area"
        className="relative w-full aspect-[5/4] rounded-xl overflow-hidden border-2 border-slate-700/70 bg-[#16152B] touch-none select-none cursor-crosshair"
      >
        <canvas
          ref={canvasRef}
          width={200}
          height={200}
          onPointerDown={handlePointerDown}
          onPointerMove={handlePointerMove}
          onPointerUp={handlePointerUp}
          onPointerCancel={handlePointerUp}
          className="w-full h-full block"
        />
        {strokes.length === 0 && stamps.length === 0 && currentPoints.length === 0 && (
          <div className="pointer-events-none absolute inset-0 flex items-center justify-center p-4 text-center">
            <span className="text-sm text-white/40 font-medium">
              ✍️ ¡Dibuja aquí tu evento o recuerdo!
            </span>
          </div>
        )}
      </div>
    </div>
  );
};
