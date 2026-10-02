import React from 'react';
import { Rocket, TrendingUp, X } from 'lucide-react';

interface MarketInnovationDialogProps {
  onDismiss: () => void;
}

export const MarketInnovationDialog: React.FC<MarketInnovationDialogProps> = ({ onDismiss }) => {
  const competitors = [
    {
      name: 'Google Calendar / Apple Calendar',
      desc: 'El estándar dominante en empresas y smartphones.',
      flaw: 'Deficiencias: Son rígidos, impersonales y fríos. Sus alarmas son pitidos monótonos que la gente suele posponer o ignorar. Cero dibujo creativo ni memoria emocional.',
    },
    {
      name: 'TimeTree / FamCal',
      desc: 'Líderes en calendarios compartidos para parejas y familias.',
      flaw: 'Deficiencias: Se limitan a cajas de texto y colores planos. No cuentan con un agente conversacional que te hable por voz ni un lienzo de dibujo íntimo.',
    },
    {
      name: 'Notion / Todoist / TickTick',
      desc: 'Herramientas de productividad y listas de tareas.',
      flaw: 'Deficiencias: Curva de aprendizaje alta. La experiencia es analítica de trabajo, no de recuerdos humanos ni fechas sentimentales.',
    },
    {
      name: 'Finch / Pi / Habitica',
      desc: 'Compañeros virtuales y mascotas gamificadas.',
      flaw: 'Deficiencias: Enfocados en autocuidado o chat general, no operan como una agenda formal de fechas importantes con alarmas exactas habladas.',
    },
  ];

  const pillars = [
    {
      title: '🎙️ 1. Alarmas con Voz Humana y Personalidad',
      detail:
        "En lugar de una alarma estresante, el agente te habla en voz alta adaptándose al tono (Cálido, Motivador, Ejecutivo o Poético): '¡Hola! Hoy es el cumpleaños de mamá, ¡acuérdate de felicitarla con todo tu cariño!'.",
    },
    {
      title: "🎨 2. 'Dibuja el Evento' (Vínculo Táctil)",
      detail:
        'La neurociencia demuestra que el ser humano recuerda 6 veces más aquello que dibuja con su mano. Cada fecha tiene su trazo, icono o boceto original del usuario.',
    },
    {
      title: '🕰️ 3. Cápsula de Recuerdos Dual',
      detail:
        'Una agenda normal solo mira al futuro y se desecha al pasar. VoxAgenda convierte cada evento cumplido en una cápsula nostálgica con notas de reflexión.',
    },
    {
      title: '☕ 4. Briefing Matutino Proactivo',
      detail:
        'El usuario no tiene que abrir menús complejos: presiona un botón mientras toma su café y el agente le relata su día completo en 25 segundos.',
    },
  ];

  return (
    <div className="fixed inset-0 z-50 bg-[#0D0C1D] flex flex-col overflow-hidden">
      <header className="flex items-center gap-2 px-4 py-3 bg-[#16152B] border-b border-slate-800 shrink-0">
        <button
          type="button"
          data-testid="close_market_dialog"
          onClick={onDismiss}
          className="p-2 rounded-full text-slate-300 hover:bg-slate-800 transition-colors"
        >
          <X className="w-5 h-5" />
        </button>
        <h2 className="text-base font-bold text-slate-100">Estudio de Mercado e Innovación</h2>
      </header>

      <div className="flex-1 overflow-y-auto p-4 max-w-2xl w-full mx-auto space-y-4 pb-12">
        {/* Intro Banner */}
        <div className="rounded-[18px] bg-indigo-900/40 border border-indigo-500/30 p-4 flex items-center gap-3.5">
          <TrendingUp className="w-9 h-9 text-indigo-400 shrink-0" />
          <div>
            <h3 className="text-base font-extrabold text-slate-100">
              ¿Qué existe en el mercado y cómo innovar?
            </h3>
            <p className="text-xs text-slate-300 mt-0.5">
              Análisis comparativo de competidores y estrategias ganadoras para hacer que este
              agente sea único en el mundo.
            </p>
          </div>
        </div>

        {/* 1. Existing market products */}
        <h3 className="text-base font-bold text-indigo-400 pt-1">1. Estado Actual del Mercado</h3>
        <div className="space-y-2.5">
          {competitors.map((c) => (
            <div
              key={c.name}
              className="rounded-2xl bg-[#222040]/45 border border-slate-800 p-3.5 space-y-1"
            >
              <h4 className="text-sm font-bold text-slate-100">{c.name}</h4>
              <p className="text-xs text-slate-400">{c.desc}</p>
              <p className="text-xs text-rose-400 pt-0.5">{c.flaw}</p>
            </div>
          ))}
        </div>

        {/* 2. How to innovate & dominate */}
        <h3 className="text-base font-bold text-indigo-400 pt-2">
          2. Cómo Superar a la Competencia (Tu Ventaja)
        </h3>
        <div className="space-y-2.5">
          {pillars.map((p) => (
            <div
              key={p.title}
              className="rounded-2xl bg-indigo-950/35 border border-indigo-500/25 p-3.5 space-y-1"
            >
              <h4 className="text-sm font-bold text-indigo-300">{p.title}</h4>
              <p className="text-xs text-slate-200 leading-relaxed">{p.detail}</p>
            </div>
          ))}
        </div>

        {/* 3. Roadmap */}
        <h3 className="text-base font-bold text-indigo-400 pt-2">
          3. Hoja de Ruta para Escalar al Máximo
        </h3>
        <div className="rounded-2xl bg-pink-950/30 border border-pink-500/25 p-4 space-y-2 text-xs text-slate-200">
          <div className="flex items-center gap-2 font-bold text-sm text-pink-300">
            <Rocket className="w-4 h-4" />
            <span>Estrategias de Crecimiento:</span>
          </div>
          <p>
            • 🗣️ Clonación de voz: Permitir que un familiar (pareja, madre, abuela) grabe una
            muestra para que sus voces te recuerden las fechas importantes.
          </p>
          <p>
            • 🪄 Foto a Boceto: Convertir una foto real en trazo de acuarela o dibujo artístico
            automático.
          </p>
          <p>
            • 🎁 Asistente de Detalles: Sugerencia inteligente de flores, regalos o cartas para
            aniversarios y cumpleaños.
          </p>
          <p>
            • 📱 Widget de Pantalla de Inicio: Mostrar el dibujo o foto del día con botón de escucha
            rápida.
          </p>
        </div>

        {/* 4. Monetization Strategy */}
        <h3 className="text-base font-bold text-indigo-400 pt-2">
          4. Plan de Monetización y Negocio
        </h3>
        <div className="rounded-2xl bg-[#222040]/50 border border-slate-800 p-4 space-y-3 text-xs">
          <h4 className="text-sm font-bold text-slate-100">
            💡 ¿Cómo genera dinero esta aplicación?
          </h4>

          <div className="space-y-1">
            <p className="font-bold text-indigo-400">
              1. Suscripción Freemium (Aura VIP - $3.99/mes o $29.99/año):
            </p>
            <p className="text-slate-300">
              • Gratis: Eventos ilimitados, fotos de galería, dibujos manuales y voz estándar.
            </p>
            <p className="text-slate-300">
              • VIP: Bocetos ilimitados con IA, voces ultra-realistas, clonación de voz y respaldo
              en la nube.
            </p>
          </div>

          <div className="space-y-1">
            <p className="font-bold text-pink-400">
              2. Comisiones por Regalos y Experiencias (8% - 15%):
            </p>
            <p className="text-slate-300">
              • La app conoce las fechas clave con anticipación (aniversario, cumple de mamá). El
              agente puede sugerir reservar flores, chocolates o cenas con 1 toque mediante
              afiliación (Amazon, floristerías o Uber Eats).
            </p>
          </div>

          <div className="space-y-1">
            <p className="font-bold text-emerald-400">
              3. Álbum Físico de Recuerdos (Print-on-demand):
            </p>
            <p className="text-slate-300">
              • Al final del año, el usuario puede pedir la impresión en tapa dura de su
              &lsquo;Cápsula Anual&rsquo; con sus dibujos, fotos y reflexiones para regalar a su
              familia.
            </p>
          </div>

          <div className="rounded-xl bg-rose-950/40 border border-rose-500/30 p-2.5 text-rose-300">
            ⚠️ Qué NO hacer: No colocar banners de publicidad intrusiva. En apps sentimentales, la
            publicidad rompe la confianza y el afecto con el agente personal.
          </div>
        </div>

        <button
          type="button"
          data-testid="btn_close_market_bottom"
          onClick={onDismiss}
          className="w-full py-3 rounded-full bg-indigo-500 hover:bg-indigo-400 text-slate-950 font-bold text-sm transition-colors"
        >
          ¡Excelente! Entendido
        </button>
      </div>
    </div>
  );
};
