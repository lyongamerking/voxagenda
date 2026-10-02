import React, { useState } from 'react';
import { Copy, ShoppingBag, Star, X } from 'lucide-react';
import { AgendaEvent, SampleGiftCatalog } from '../types/agenda';

interface GiftAffiliateDialogProps {
  targetEvent: AgendaEvent | null;
  onDismiss: () => void;
}

export const GiftAffiliateDialog: React.FC<GiftAffiliateDialogProps> = ({
  targetEvent,
  onDismiss,
}) => {
  const [copiedCoupon, setCopiedCoupon] = useState(false);

  const handleCopyCoupon = () => {
    navigator.clipboard?.writeText('AURA10').catch(() => {});
    setCopiedCoupon(true);
  };

  return (
    <div className="fixed inset-0 z-50 bg-[#0D0C1D] flex flex-col overflow-hidden">
      <header className="flex items-center justify-between px-4 py-3 bg-[#16152B] border-b border-slate-800 shrink-0">
        <h2 className="text-base font-bold text-slate-100">
          🎁 Regalos y Flores con Descuento
        </h2>
        <button
          type="button"
          data-testid="gifts_close_btn"
          onClick={onDismiss}
          className="p-2 rounded-full text-slate-300 hover:bg-slate-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>
      </header>

      <div className="flex-1 overflow-y-auto p-4 max-w-xl w-full mx-auto space-y-4 pb-12">
        {/* Event Context Banner */}
        <div className="rounded-2xl bg-indigo-900/45 border border-indigo-500/30 p-3.5 flex items-center gap-3">
          <div className="w-10 h-10 rounded-full bg-indigo-500 flex items-center justify-center shrink-0 text-lg">
            💝
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-[11px] font-semibold text-indigo-300">Sugerencias de Aura para:</p>
            <p className="text-sm font-bold text-slate-100 truncate">
              {targetEvent?.title || 'Tu persona especial'}
            </p>
            <p className="text-xs text-slate-300">
              Envía flores o regalos con entrega a domicilio programada.
            </p>
          </div>
        </div>

        {/* Coupon code card */}
        <div className="rounded-2xl bg-amber-50 border border-amber-300 p-3.5 space-y-1.5">
          <div className="flex items-center justify-between gap-2">
            <div className="flex items-center gap-1.5">
              <Star className="w-4 h-4 text-amber-600 fill-amber-600" />
              <span className="text-xs font-bold text-amber-900">
                Cupón Exclusivo de Afiliado:
              </span>
            </div>
            <button
              type="button"
              onClick={handleCopyCoupon}
              className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg bg-amber-600 hover:bg-amber-500 text-white text-xs font-extrabold transition-colors"
            >
              <span>{copiedCoupon ? '¡COPIADO!' : 'AURA10'}</span>
              <Copy className="w-3 h-3" />
            </button>
          </div>
          <p className="text-xs text-amber-900/90 leading-relaxed">
            Aplica el código AURA10 para un 10% de descuento al pagar. La tienda rastrea
            automáticamente tu compra y acredita tu comisión.
          </p>
        </div>

        {/* Catalog */}
        <h3 className="text-sm font-bold text-slate-100">Catálogo de Socios Oficiales:</h3>

        <div className="space-y-3">
          {SampleGiftCatalog.map((gift) => (
            <div
              key={gift.title}
              className="rounded-2xl bg-[#16152B] border border-slate-800 p-3.5 space-y-2.5"
            >
              <div className="flex items-center gap-3">
                <div className="w-11 h-11 rounded-xl bg-[#222040] flex items-center justify-center text-xl shrink-0">
                  {gift.iconEmoji}
                </div>
                <div className="flex-1 min-w-0">
                  <h4 className="text-sm font-bold text-slate-100">{gift.title}</h4>
                  <p className="text-[11px] text-indigo-400">
                    {gift.category} • {gift.partnerStore}
                  </p>
                </div>
                <span className="text-sm font-extrabold text-indigo-400 shrink-0">
                  {gift.priceEstimate}
                </span>
              </div>

              <p className="text-xs text-slate-300">{gift.description}</p>

              <div className="flex items-center justify-between gap-2 pt-1">
                <span className="px-2 py-1 rounded-md bg-emerald-500/15 text-emerald-300 text-[11px] font-semibold">
                  💰 Ganancia de la App: {gift.commissionPercent}
                </span>

                <a
                  href={gift.simulatedUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="inline-flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-xs transition-colors"
                >
                  <ShoppingBag className="w-3.5 h-3.5" />
                  <span>Ver Tienda</span>
                </a>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};
