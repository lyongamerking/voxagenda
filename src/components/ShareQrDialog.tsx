import React, { useState } from 'react';
import { Copy, QrCode, Share2, X } from 'lucide-react';

interface ShareQrDialogProps {
  appUrl?: string;
  onDismiss: () => void;
}

export const ShareQrDialog: React.FC<ShareQrDialogProps> = ({
  appUrl = typeof window !== 'undefined' ? window.location.origin : 'https://voxagenda.app',
  onDismiss,
}) => {
  const [copiedMessage, setCopiedMessage] = useState(false);
  const [invitedFriendsCount, setInvitedFriendsCount] = useState(1);

  const qrApiUrl = `https://api.qrserver.com/v1/create-qr-code/?size=400x400&data=${encodeURIComponent(
    appUrl
  )}`;

  const handleCopy = () => {
    navigator.clipboard?.writeText(appUrl).catch(() => {});
    setCopiedMessage(true);
  };

  const handleShare = () => {
    const shareText = `¡Mira esta app que creé! VoxAgenda por Lyon Studio: Tu agente personal interactivo con alarmas por voz, dibujos y recuerdos familiares. Ábrela aquí: ${appUrl}`;
    if (navigator.share) {
      navigator
        .share({
          title: 'VoxAgenda por Lyon Studio',
          text: shareText,
          url: appUrl,
        })
        .catch(() => {});
    } else {
      handleCopy();
    }
  };

  return (
    <div className="fixed inset-0 z-50 bg-[#0D0C1D] flex flex-col overflow-hidden">
      <header className="flex items-center justify-between px-4 py-3 bg-[#16152B] border-b border-slate-800 shrink-0">
        <div className="flex items-center gap-2">
          <QrCode className="w-5 h-5 text-indigo-400" />
          <h2 className="text-base font-bold text-slate-100">Compartir con Código QR</h2>
        </div>
        <button
          type="button"
          data-testid="qr_close_btn"
          onClick={onDismiss}
          className="p-2 rounded-full text-slate-300 hover:bg-slate-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>
      </header>

      <div className="flex-1 overflow-y-auto p-5 max-w-xl w-full mx-auto space-y-4 pb-12 flex flex-col items-center">
        <h3 className="text-base font-bold text-slate-100 text-center">
          ¡Muestra este código para que lo escaneen!
        </h3>
        <p className="text-xs text-slate-300 text-center max-w-md">
          Cualquier persona puede apuntar con la cámara de su teléfono y abrirá VoxAgenda al
          instante sin instalar nada previo.
        </p>

        {/* QR Display Card */}
        <div
          data-testid="qr_code_card"
          className="w-64 h-64 rounded-3xl bg-white p-4 border-2 border-slate-700 shadow-xl flex items-center justify-center"
        >
          <img
            src={qrApiUrl}
            alt="Código QR de la app"
            referrerPolicy="no-referrer"
            className="w-full h-full object-contain rounded-xl"
          />
        </div>

        {/* Link Preview Box */}
        <div className="w-full rounded-xl bg-[#222040]/60 border border-slate-800 px-3.5 py-2.5 text-xs text-slate-300 truncate">
          {appUrl}
        </div>

        {/* Actions */}
        <div className="w-full flex items-center gap-2.5">
          <button
            type="button"
            data-testid="btn_copy_qr_link"
            onClick={handleCopy}
            className="flex-1 h-12 rounded-full border border-slate-700 hover:bg-slate-800 text-xs font-bold text-slate-200 flex items-center justify-center gap-2 transition-colors"
          >
            <Copy className="w-4 h-4" />
            <span>{copiedMessage ? '¡Copiado!' : 'Copiar Link'}</span>
          </button>

          <button
            type="button"
            data-testid="btn_share_whatsapp"
            onClick={handleShare}
            className="flex-1 h-12 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-xs flex items-center justify-center gap-2 transition-colors"
          >
            <Share2 className="w-4 h-4" />
            <span>Compartir</span>
          </button>
        </div>

        {/* Programa Invita y Gana */}
        <div className="w-full rounded-[20px] bg-purple-50 border-[1.5px] border-purple-300 p-4 space-y-2.5 text-purple-950">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="text-base">🎁</span>
              <span className="text-sm font-bold text-purple-900">Programa: ¡Invita y Gana!</span>
            </div>
            <span className="px-2 py-0.5 rounded-lg bg-purple-700 text-white text-xs font-bold tabular-nums">
              {invitedFriendsCount} / 3 Amigos
            </span>
          </div>

          <p className="text-xs font-bold text-purple-900">
            ¿Qué ganas tú y tu amigo al unirse?
          </p>

          <div className="space-y-1 text-xs text-purple-950">
            <p>• 👤 Tu amigo recibe: 3 Días de Aura VIP Gratis al escanear tu código.</p>
            <p>• 🎨 Si invitas a 1 amigo: Desbloqueas 5 Bocetos IA artísticos extras.</p>
            <p className="font-bold text-purple-800">
              • 👑 Si invitas a 3 amigos: ¡1 MES COMPLETO de Aura VIP GRATIS!
            </p>
          </div>

          {/* Progress Bar */}
          <div className="w-full h-2 rounded-full bg-purple-200 overflow-hidden">
            <div
              className="h-full bg-purple-700 transition-all duration-300"
              style={{ width: `${Math.min((invitedFriendsCount / 3) * 100, 100)}%` }}
            />
          </div>

          {invitedFriendsCount >= 3 ? (
            <div className="rounded-xl bg-emerald-500/20 border border-emerald-600 p-2.5 flex items-center gap-2 text-xs font-bold text-emerald-900">
              <span>🎉</span>
              <span>¡Felicidades! Meta alcanzada. Reclama tu Mes VIP Gratis.</span>
            </div>
          ) : (
            <button
              type="button"
              onClick={() => setInvitedFriendsCount((c) => Math.min(c + 1, 3))}
              className="w-full py-2 rounded-full border border-purple-400 hover:bg-purple-100 text-xs font-semibold text-purple-900 transition-colors"
            >
              Simular amigo escaneando QR (+1)
            </button>
          )}
        </div>

        {/* Helpful Tip */}
        <div className="w-full rounded-2xl bg-blue-50 border border-blue-200 p-3.5 text-xs text-blue-900 space-y-1">
          <p className="font-bold text-blue-950">💡 Consejo para compartir y ganar:</p>
          <p>
            Puedes imprimir este código QR en una hoja o tarjeta de presentación. Cuando la gente lo
            escanee, entrará a tu app y cualquier compra de regalos o libros generará tu comisión.
          </p>
        </div>
      </div>
    </div>
  );
};
