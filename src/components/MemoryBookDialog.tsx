import React, { useState } from 'react';
import { BookOpen, CheckCircle2, CloudDownload, Truck, X } from 'lucide-react';
import { AgendaEvent, BookFormatInfo, BookFormatOptions } from '../types/agenda';

interface MemoryBookDialogProps {
  memories: AgendaEvent[];
  onDismiss: () => void;
}

export const MemoryBookDialog: React.FC<MemoryBookDialogProps> = ({ memories, onDismiss }) => {
  const [selectedFormat, setSelectedFormat] = useState<BookFormatInfo>(BookFormatOptions[1]);
  const [recipientName, setRecipientName] = useState('');
  const [emailAddress, setEmailAddress] = useState('');
  const [shippingAddress, setShippingAddress] = useState('');
  const [orderSubmitted, setOrderSubmitted] = useState(false);

  const coverGradient =
    selectedFormat.id === 'DIGITAL'
      ? 'from-teal-700 to-teal-500 border-white/50'
      : selectedFormat.id === 'SOFTCOVER'
      ? 'from-blue-700 to-blue-500 border-white/50'
      : 'from-indigo-900 via-indigo-700 to-indigo-500 border-amber-300';

  return (
    <div className="fixed inset-0 z-50 bg-[#0D0C1D] flex flex-col overflow-hidden">
      <header className="flex items-center justify-between px-4 py-3 bg-[#16152B] border-b border-slate-800 shrink-0">
        <div className="flex items-center gap-2">
          <BookOpen className="w-5 h-5 text-indigo-400" />
          <h2 className="text-base font-bold text-slate-100">Libro de Recuerdos</h2>
        </div>
        <button
          type="button"
          data-testid="book_close_btn"
          onClick={onDismiss}
          className="p-2 rounded-full text-slate-300 hover:bg-slate-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>
      </header>

      <div className="flex-1 overflow-y-auto p-4 max-w-xl w-full mx-auto space-y-4 pb-12 flex flex-col items-center">
        {/* Book Mockup Showcase */}
        <div
          className={`w-[220px] h-[280px] rounded-r-2xl rounded-l-sm bg-gradient-to-b ${coverGradient} border-2 p-4 shadow-2xl flex flex-col items-center justify-between text-center`}
        >
          <div className="w-11 h-11 rounded-full bg-white/20 flex items-center justify-center text-xl">
            {selectedFormat.iconEmoji}
          </div>

          <div>
            <h3
              className={`text-base font-extrabold uppercase ${
                selectedFormat.id === 'HARDCOVER' ? 'text-amber-300' : 'text-white'
              }`}
            >
              {recipientName.trim() ? recipientName.toUpperCase() : 'NUESTRA HISTORIA'}
            </h3>
            <p className="text-xs text-white/90 font-medium">Cápsula de Memorias</p>
            <p className="text-[11px] text-white/80 mt-2.5">
              {memories.length} Recuerdos y bocetos incluidos
            </p>
          </div>

          <span className="text-[10px] tracking-wider uppercase text-white/90 font-semibold">
            {selectedFormat.title}
          </span>
        </div>

        {/* Format Selection */}
        <div className="w-full space-y-2">
          <h4 className="text-sm font-bold text-indigo-400">Elige tu formato preferido:</h4>
          {BookFormatOptions.map((option) => {
            const isSelected = selectedFormat.id === option.id;
            return (
              <div
                key={option.id}
                data-testid={`format_${option.id}`}
                onClick={() => {
                  setSelectedFormat(option);
                  setOrderSubmitted(false);
                }}
                className={`w-full rounded-2xl p-3 border cursor-pointer flex items-center gap-3 transition-all ${
                  isSelected
                    ? 'border-2 border-indigo-400 bg-indigo-500/10'
                    : 'border-slate-800 bg-[#16152B] hover:border-slate-700'
                }`}
              >
                <span className="text-xl">{option.iconEmoji}</span>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-bold text-slate-100">{option.title}</p>
                  <p className="text-xs text-slate-400">{option.subtitle}</p>
                </div>
                <div className="text-right shrink-0">
                  <p className="text-sm font-extrabold text-indigo-400">{option.priceText}</p>
                  {!option.isPhysical && (
                    <span className="inline-block px-1.5 py-0.5 rounded bg-emerald-500 text-white text-[10px] font-bold">
                      INMEDIATO
                    </span>
                  )}
                </div>
              </div>
            );
          })}
        </div>

        {/* Profit & Price Breakdown */}
        <div className="w-full rounded-2xl bg-emerald-50 border border-emerald-300 p-3.5 space-y-1 text-xs">
          <div className="flex items-center justify-between font-bold text-emerald-950">
            <span>Formato: {selectedFormat.title}</span>
            <span className="text-sm font-extrabold text-emerald-700">
              {selectedFormat.priceText}
            </span>
          </div>
          <div className="flex items-center justify-between text-emerald-800">
            <span>Costo de producción:</span>
            <span>{selectedFormat.printCost}</span>
          </div>
          <div className="flex items-center justify-between font-bold text-emerald-700">
            <span>Tu Ganancia Neta:</span>
            <span>{selectedFormat.netProfit}</span>
          </div>
        </div>

        {/* Order Simulator Form */}
        {orderSubmitted ? (
          <div className="w-full rounded-2xl bg-emerald-500/15 border border-emerald-500 p-4 flex flex-col items-center text-center space-y-1.5">
            <CheckCircle2 className="w-9 h-9 text-emerald-400" />
            <p className="text-sm font-bold text-emerald-300">
              {!selectedFormat.isPhysical
                ? '¡Archivo Digital Generado!'
                : '¡Pedido Físico Confirmado!'}
            </p>
            <p className="text-xs text-emerald-200/90">
              {!selectedFormat.isPhysical
                ? 'Tu PDF / ePub en alta resolución de 300 DPI está listo para descargar o imprimir donde prefieras.'
                : 'La orden ha sido enviada a la imprenta para fabricación y envío con guía de rastreo a tu domicilio.'}
            </p>
          </div>
        ) : (
          <div className="w-full space-y-3">
            <div>
              <label className="block text-xs font-medium text-slate-300 mb-1">
                Nombre para la Portada / Dedicatoria
              </label>
              <input
                type="text"
                data-testid="book_recipient_input"
                value={recipientName}
                onChange={(e) => setRecipientName(e.target.value)}
                placeholder="Ej: Nuestra Familia, Para Mamá..."
                className="w-full px-3.5 py-2.5 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-indigo-400"
              />
            </div>

            {!selectedFormat.isPhysical ? (
              <>
                <div>
                  <label className="block text-xs font-medium text-slate-300 mb-1">
                    Correo Electrónico para Recibir el Archivo
                  </label>
                  <input
                    type="email"
                    data-testid="book_email_input"
                    value={emailAddress}
                    onChange={(e) => setEmailAddress(e.target.value)}
                    placeholder="ejemplo@gmail.com"
                    className="w-full px-3.5 py-2.5 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-indigo-400"
                  />
                </div>
                <button
                  type="button"
                  data-testid="book_order_btn"
                  onClick={() => setOrderSubmitted(true)}
                  className="w-full h-12 rounded-2xl bg-teal-700 hover:bg-teal-600 text-white font-bold text-xs flex items-center justify-center gap-2 transition-colors"
                >
                  <CloudDownload className="w-4 h-4" />
                  <span>Generar y Descargar ePub / PDF ($5.99 USD)</span>
                </button>
              </>
            ) : (
              <>
                <div className="rounded-xl bg-pink-950/40 border border-pink-500/25 p-3 flex items-start gap-2.5">
                  <BookOpen className="w-5 h-5 text-indigo-400 shrink-0 mt-0.5" />
                  <div>
                    <p className="text-xs font-bold text-indigo-300">
                      📖 Editorial Acuario • Impresión Física
                    </p>
                    <p className="text-xs text-pink-200/85 mt-0.5">
                      Catálogo de impresión física en pasta dura y suave próximamente disponible en
                      el portal web oficial. Mientras tanto, puedes descargar tu Libro Digital.
                    </p>
                  </div>
                </div>

                <div>
                  <label className="block text-xs font-medium text-slate-300 mb-1">
                    Dirección de Envío Completa
                  </label>
                  <textarea
                    rows={2}
                    data-testid="book_address_input"
                    value={shippingAddress}
                    onChange={(e) => setShippingAddress(e.target.value)}
                    placeholder="Calle, Número, Ciudad, Código Postal, País"
                    className="w-full px-3.5 py-2.5 rounded-xl bg-[#16152B] border border-slate-700 text-xs text-slate-100 placeholder:text-slate-500 focus:outline-none focus:border-indigo-400"
                  />
                </div>

                <button
                  type="button"
                  data-testid="book_order_btn"
                  onClick={() => setOrderSubmitted(true)}
                  className="w-full h-12 rounded-2xl bg-indigo-600 hover:bg-indigo-500 text-white font-bold text-xs flex items-center justify-center gap-2 transition-colors"
                >
                  <Truck className="w-4 h-4" />
                  <span>
                    Ordenar {selectedFormat.title} ({selectedFormat.priceText})
                  </span>
                </button>
              </>
            )}
          </div>
        )}
      </div>
    </div>
  );
};
