import React, { useState } from 'react';
import {
  Award,
  CheckCircle2,
  Cloud,
  Gem,
  Mic,
  Sparkles,
  Star,
  X,
} from 'lucide-react';

interface AuraVipDialogProps {
  isVipActive: boolean;
  onToggleVip: (active: boolean) => void;
  onDismiss: () => void;
}

export const AuraVipDialog: React.FC<AuraVipDialogProps> = ({
  isVipActive,
  onToggleVip,
  onDismiss,
}) => {
  const [isAnnualSelected, setIsAnnualSelected] = useState(true);
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);

  const amount = isAnnualSelected ? '29.99' : '3.99';
  const itemName = isAnnualSelected
    ? 'Aura VIP Anual (VoxAgenda)'
    : 'Aura VIP Mensual (VoxAgenda)';
  const paypalUrl = `https://www.paypal.com/cgi-bin/webscr?cmd=_xclick&business=acuarioeditorial@gmail.com&item_name=${encodeURIComponent(
    itemName
  )}&amount=${amount}&currency_code=USD`;

  return (
    <div className="fixed inset-0 z-50 bg-[#0D0C1D] flex flex-col overflow-hidden">
      <header className="flex items-center justify-between px-4 py-3 bg-[#16152B] border-b border-slate-800 shrink-0">
        <div className="flex items-center gap-2">
          <Award className="w-6 h-6 text-amber-400" />
          <h2 className="text-base font-bold text-slate-100">Aura VIP Club</h2>
        </div>
        <button
          type="button"
          data-testid="vip_close_btn"
          onClick={onDismiss}
          className="p-2 rounded-full text-slate-300 hover:bg-slate-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>
      </header>

      <div className="flex-1 overflow-y-auto p-5 max-w-xl w-full mx-auto space-y-5 pb-12">
        {/* Header Banner */}
        <div className="rounded-3xl bg-gradient-to-r from-indigo-500 via-purple-500 to-pink-500 p-6 text-center flex flex-col items-center shadow-lg">
          <div className="w-14 h-14 rounded-full bg-white/20 flex items-center justify-center mb-3">
            <Gem className="w-8 h-8 text-amber-300" />
          </div>
          <h3 className="text-xl font-extrabold text-white">Desbloquea el poder total de Aura</h3>
          <p className="text-xs text-white/90 mt-1.5 max-w-md">
            Preserva los momentos más valiosos de tu vida con tecnología emocional exclusiva.
          </p>
        </div>

        {/* VIP Benefits */}
        <div className="rounded-[20px] bg-[#222040]/50 border border-slate-800 p-4 space-y-3.5">
          <h4 className="text-xs font-bold text-indigo-400">
            Beneficios Exclusivos de la Suscripción:
          </h4>

          <div className="flex items-start gap-3">
            <div className="w-9 h-9 rounded-full bg-rose-500/15 flex items-center justify-center shrink-0">
              <Mic className="w-4 h-4 text-rose-400" />
            </div>
            <div>
              <p className="text-sm font-bold text-slate-100">🗣️ Clonación de Voz Emocional</p>
              <p className="text-xs text-slate-400">
                Graba la voz real de tu mamá, tu pareja o tus hijos para que sus voces te despierten
                y feliciten.
              </p>
            </div>
          </div>

          <div className="flex items-start gap-3">
            <div className="w-9 h-9 rounded-full bg-indigo-500/15 flex items-center justify-center shrink-0">
              <Sparkles className="w-4 h-4 text-indigo-400" />
            </div>
            <div>
              <p className="text-sm font-bold text-slate-100">
                ✨ Arte &amp; Bocetos por IA Ilimitados
              </p>
              <p className="text-xs text-slate-400">
                Convierte cada fecha en una acuarela, boceto o pintura con estilos artísticos
                premium sin límites.
              </p>
            </div>
          </div>

          <div className="flex items-start gap-3">
            <div className="w-9 h-9 rounded-full bg-emerald-500/15 flex items-center justify-center shrink-0">
              <Cloud className="w-4 h-4 text-emerald-400" />
            </div>
            <div>
              <p className="text-sm font-bold text-slate-100">☁️ Respaldo Infinito en la Nube</p>
              <p className="text-xs text-slate-400">
                Tus fotos, audios y dibujos estarán a salvo para siempre aunque cambies de móvil.
              </p>
            </div>
          </div>

          <div className="flex items-start gap-3">
            <div className="w-9 h-9 rounded-full bg-amber-500/15 flex items-center justify-center shrink-0">
              <Star className="w-4 h-4 text-amber-400" />
            </div>
            <div>
              <p className="text-sm font-bold text-slate-100">
                🏷️ Descuentos VIP en Flores y Libros
              </p>
              <p className="text-xs text-slate-400">
                15% de descuento permanente en envíos de flores y en el libro físico de recuerdos.
              </p>
            </div>
          </div>
        </div>

        {/* Pricing Toggle */}
        <div className="grid grid-cols-2 gap-1.5 p-1 rounded-2xl bg-[#222040]/60 border border-slate-800">
          <button
            type="button"
            onClick={() => setIsAnnualSelected(true)}
            className={`py-3 px-2 rounded-xl text-center transition-colors ${
              isAnnualSelected
                ? 'bg-indigo-500 text-slate-950'
                : 'text-slate-300 hover:bg-slate-800/50'
            }`}
          >
            <p className="text-xs font-extrabold">ANUAL (Ahorra 37%)</p>
            <p className="text-[11px] opacity-85 mt-0.5">$29.99 / año ($2.49/mes)</p>
          </button>

          <button
            type="button"
            onClick={() => setIsAnnualSelected(false)}
            className={`py-3 px-2 rounded-xl text-center transition-colors ${
              !isAnnualSelected
                ? 'bg-indigo-500 text-slate-950'
                : 'text-slate-300 hover:bg-slate-800/50'
            }`}
          >
            <p className="text-xs font-bold">MENSUAL</p>
            <p className="text-[11px] opacity-85 mt-0.5">$3.99 / mes</p>
          </button>
        </div>

        {/* Action Button */}
        {isVipActive ? (
          <div className="rounded-2xl bg-emerald-500/15 border border-emerald-500 p-4 flex items-center gap-3">
            <CheckCircle2 className="w-6 h-6 text-emerald-400 shrink-0" />
            <div className="flex-1">
              <p className="text-sm font-bold text-emerald-300">¡Membresía VIP Activa!</p>
              <p className="text-xs text-emerald-200/80">
                Tienes acceso completo a todas las funciones premium.
              </p>
            </div>
            <button
              type="button"
              onClick={() => onToggleVip(false)}
              className="px-3.5 py-1.5 rounded-xl bg-rose-600 hover:bg-rose-500 text-white text-xs font-bold transition-colors"
            >
              Pausar
            </button>
          </div>
        ) : (
          <div className="space-y-2.5">
            <a
              href={paypalUrl}
              target="_blank"
              rel="noopener noreferrer"
              data-testid="paypal_vip_btn"
              onClick={() => {
                onToggleVip(true);
                setShowSuccessMessage(true);
              }}
              className="w-full h-14 rounded-2xl bg-[#0070BA] hover:bg-[#005ea6] text-white font-bold text-sm flex items-center justify-center gap-2 transition-colors shadow-md"
            >
              <Award className="w-5 h-5 text-amber-300" />
              <span>
                {isAnnualSelected
                  ? 'Pagar con PayPal ($29.99/año)'
                  : 'Pagar con PayPal ($3.99/mes)'}
              </span>
            </a>

            <button
              type="button"
              data-testid="subscribe_vip_btn"
              onClick={() => {
                onToggleVip(true);
                setShowSuccessMessage(true);
              }}
              className="w-full h-12 rounded-2xl bg-pink-900/60 hover:bg-pink-900/80 text-pink-100 font-medium text-xs flex items-center justify-center gap-2 transition-colors"
            >
              <Sparkles className="w-4 h-4" />
              <span>Activar Demostración VIP Gratuita</span>
            </button>
          </div>
        )}

        {showSuccessMessage && (
          <div className="rounded-xl bg-emerald-500/15 border border-emerald-500/30 p-3 text-center text-xs text-emerald-300">
            🎉 ¡Felicidades! Se ha activado la simulación de suscripción Aura VIP. Todas las
            funciones prémium están desbloqueadas.
          </div>
        )}

        <p className="text-[11px] text-slate-400 text-center">
          🔒 Pago 100% seguro. Cancela en cualquier momento con un solo toque desde tus ajustes.
        </p>
      </div>
    </div>
  );
};
