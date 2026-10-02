export interface DoodlePoint {
  x: number;
  y: number;
}

export interface DoodleStroke {
  points: DoodlePoint[];
  colorHex: string;
  strokeWidth: number;
}

export interface DoodleStamp {
  x: number;
  y: number;
  symbol: string;
  size: number;
}

export interface DoodleDrawing {
  strokes: DoodleStroke[];
  stamps: DoodleStamp[];
  bgColorHex: string;
}

export type VisualType = 'DOODLE' | 'PHOTO' | 'AI_ART';

export interface AgendaEvent {
  id: number;
  title: string;
  description: string;
  dateTimeEpochMs: number;
  category: string;
  importance: string;
  visualType: VisualType;
  doodleJson: string;
  photoUri: string;
  aiArtStyle: string;
  voiceMessageCustom: string;
  voicePersona: string;
  isAlarmEnabled: boolean;
  isCompleted: boolean;
  isMemoryCapsule: boolean;
  reflectionNote: string;
  creationTimestamp: number;
}

export const CategoriesList: string[] = [
  'Cumpleaños',
  'Aniversario',
  'Cita Médica',
  'Meta Personal',
  'Viaje',
  'Recuerdo',
  'Trabajo',
  'Familia',
];

export const ImportanceLevels: string[] = ['Prioritaria', 'Alta', 'Normal'];

export const Personas: Array<{ key: string; label: string }> = [
  { key: 'Cálida', label: '🌟 Cálida & Afectuosa' },
  { key: 'Motivadora', label: '🚀 Motivadora & Enérgica' },
  { key: 'Ejecutiva', label: '💼 Ejecutiva & Precisa' },
  { key: 'Poética', label: '🌸 Poética & Reflexiva' },
];

export const AiArtStylesList: Array<{ name: string; imageUrl: string }> = [
  { name: 'Celebración & Acuarela', imageUrl: '/assets/ai_art_celebration.png' },
  { name: 'Paisaje & Escapada', imageUrl: '/assets/ai_art_scenic.png' },
];

export const DoodleColors: string[] = [
  '#F59E0B', // Amber Gold
  '#EC4899', // Rose Pink
  '#06B6D4', // Cyan
  '#A855F7', // Purple
  '#10B981', // Emerald
  '#EF4444', // Red Coral
  '#84CC16', // Lime
  '#FFFFFF', // White
];

export const DoodleStamps: string[] = ['🎂', '❤️', '⭐', '🎉', '✈️', '💍', '🏆', '🌸', '💡', '🎵'];

export interface GiftItem {
  title: string;
  category: string;
  priceEstimate: string;
  iconEmoji: string;
  description: string;
  partnerStore: string;
  commissionPercent: string;
  simulatedUrl: string;
}

export const SampleGiftCatalog: GiftItem[] = [
  {
    title: 'Ramo de Rosas & Lilis Primaverales',
    category: 'Flores Frescas',
    priceEstimate: '$38.00 USD',
    iconEmoji: '💐',
    description:
      'Arreglo floral fresco entregado a domicilio en caja de lujo con tarjeta personalizada.',
    partnerStore: 'EnvíaFlores / Interflora Partner',
    commissionPercent: '12% de comisión',
    simulatedUrl: 'https://www.enviaflores.com?ref=voxagenda',
  },
  {
    title: 'Desayuno Sorpresa & Globos Festivos',
    category: 'Experiencias',
    priceEstimate: '$45.00 USD',
    iconEmoji: '🥐',
    description:
      'Canasta con croissant recién horneado, café de especialidad, jugo y globos temáticos.',
    partnerStore: 'Gourmet Express Partner',
    commissionPercent: '10% de comisión',
    simulatedUrl: 'https://www.gourmetexpress.com?ref=voxagenda',
  },
  {
    title: 'Cofre de Chocolates Belgas Artesanales',
    category: 'Dulces & Delicias',
    priceEstimate: '$26.00 USD',
    iconEmoji: '🍫',
    description:
      'Selección de 24 trufas artesanales con licor y frutos secos para endulzar el día.',
    partnerStore: 'Chocolaterie Partner',
    commissionPercent: '15% de comisión',
    simulatedUrl: 'https://www.chocolates.com?ref=voxagenda',
  },
  {
    title: 'Dije de Plata Grabado con Fecha Especial',
    category: 'Joyería Emocional',
    priceEstimate: '$52.00 USD',
    iconEmoji: '💍',
    description:
      'Colgante de plata esterlina con la fecha del evento grabada en alta precisión.',
    partnerStore: 'Joyas & Memorias Partner',
    commissionPercent: '14% de comisión',
    simulatedUrl: 'https://www.joyas.com?ref=voxagenda',
  },
];

export interface BookFormatInfo {
  id: 'DIGITAL' | 'SOFTCOVER' | 'HARDCOVER';
  title: string;
  subtitle: string;
  priceText: string;
  printCost: string;
  netProfit: string;
  iconEmoji: string;
  isPhysical: boolean;
}

export const BookFormatOptions: BookFormatInfo[] = [
  {
    id: 'DIGITAL',
    title: 'Digital (PDF / ePub HD)',
    subtitle: 'Descarga inmediata para imprimir por tu cuenta o compartir en familia.',
    priceText: '$5.99 USD',
    printCost: '$0.00 USD (Cero costo)',
    netProfit: '+$5.99 USD (100% ganancia)',
    iconEmoji: '📱',
    isPhysical: false,
  },
  {
    id: 'SOFTCOVER',
    title: 'Tapa Blanda Económica',
    subtitle: 'Encuadernación flexible, papel fotográfico premium, envío a domicilio.',
    priceText: '$18.99 USD',
    printCost: '$6.50 USD (Imprenta)',
    netProfit: '+$12.49 USD de ganancia',
    iconEmoji: '📘',
    isPhysical: true,
  },
  {
    id: 'HARDCOVER',
    title: 'Pasta Dura Deluxe',
    subtitle: 'Acabado de lujo en relieve, laminado mate y estuche protector.',
    priceText: '$28.99 USD',
    printCost: '$10.50 USD (Imprenta)',
    netProfit: '+$18.49 USD de ganancia',
    iconEmoji: '📕',
    isPhysical: true,
  },
];

export function serializeDoodle(drawing: DoodleDrawing): string {
  try {
    const root = {
      bg: drawing.bgColorHex || '#16152B',
      strokes: drawing.strokes.map((s) => ({
        c: s.colorHex,
        w: s.strokeWidth,
        pts: s.points.map((pt) => ({ x: pt.x, y: pt.y })),
      })),
      stamps: drawing.stamps.map((st) => ({
        x: st.x,
        y: st.y,
        sym: st.symbol,
        sz: st.size,
      })),
    };
    return JSON.stringify(root);
  } catch {
    return '{}';
  }
}

export function parseDoodle(jsonStr?: string | null): DoodleDrawing {
  const empty: DoodleDrawing = {
    strokes: [],
    stamps: [],
    bgColorHex: '#16152B',
  };
  if (!jsonStr || jsonStr.trim() === '' || jsonStr === '{}') {
    return empty;
  }
  try {
    const root = JSON.parse(jsonStr);
    const bg = typeof root.bg === 'string' ? root.bg : '#16152B';
    const strokes: DoodleStroke[] = Array.isArray(root.strokes)
      ? root.strokes.map((s: any) => ({
          colorHex: typeof s.c === 'string' ? s.c : '#FFFFFF',
          strokeWidth: typeof s.w === 'number' ? s.w : 5,
          points: Array.isArray(s.pts)
            ? s.pts.map((p: any) => ({
                x: typeof p.x === 'number' ? p.x : 0,
                y: typeof p.y === 'number' ? p.y : 0,
              }))
            : [],
        }))
      : [];
    const stamps: DoodleStamp[] = Array.isArray(root.stamps)
      ? root.stamps.map((st: any) => ({
          x: typeof st.x === 'number' ? st.x : 0,
          y: typeof st.y === 'number' ? st.y : 0,
          symbol: typeof st.sym === 'string' ? st.sym : '⭐',
          size: typeof st.sz === 'number' ? st.sz : 28,
        }))
      : [];
    return { strokes, stamps, bgColorHex: bg };
  } catch {
    return empty;
  }
}

export function getEffectiveVoiceMessage(event: Pick<AgendaEvent, 'title' | 'importance' | 'voiceMessageCustom' | 'voicePersona'>): string {
  if (event.voiceMessageCustom && event.voiceMessageCustom.trim().length > 0) {
    return event.voiceMessageCustom.trim();
  }
  switch (event.voicePersona) {
    case 'Motivadora':
      return `¡Atención! Aura reportándose. Ha llegado el momento de ${event.title}. ¡A darlo todo con la mejor actitud y energía!`;
    case 'Ejecutiva':
      return `Recordatorio de agenda: ${event.title}. Importancia ${event.importance || 'Alta'}. Por favor verifique sus preparativos a tiempo.`;
    case 'Poética':
      return `Un instante memorable florece en tu día: ${event.title}. Disfruta cada segundo y guárdalo en tu corazón.`;
    default:
      return `¡Hola! Soy tu asistente Aura. Te recuerdo con mucho cariño tu evento especial: ${event.title}. ¡Que tengas un día grandioso!`;
  }
}

export function createSampleCake(): DoodleDrawing {
  return {
    bgColorHex: '#1E1A38',
    strokes: [
      {
        points: [
          { x: 40, y: 150 },
          { x: 160, y: 150 },
          { x: 155, y: 180 },
          { x: 45, y: 180 },
          { x: 40, y: 150 },
        ],
        colorHex: '#F472B6',
        strokeWidth: 8,
      },
      {
        points: [
          { x: 60, y: 120 },
          { x: 140, y: 120 },
          { x: 140, y: 150 },
          { x: 60, y: 150 },
          { x: 60, y: 120 },
        ],
        colorHex: '#60A5FA',
        strokeWidth: 8,
      },
      {
        points: [
          { x: 100, y: 120 },
          { x: 100, y: 95 },
        ],
        colorHex: '#FBBF24',
        strokeWidth: 6,
      },
      {
        points: [
          { x: 100, y: 92 },
          { x: 100, y: 85 },
        ],
        colorHex: '#EF4444',
        strokeWidth: 10,
      },
    ],
    stamps: [
      { x: 100, y: 82, symbol: '✨', size: 22 },
      { x: 35, y: 125, symbol: '🎂', size: 24 },
      { x: 165, y: 125, symbol: '🎉', size: 24 },
    ],
  };
}

export function createSampleBeach(): DoodleDrawing {
  return {
    bgColorHex: '#0F172A',
    strokes: [
      {
        points: [
          { x: 60, y: 60 },
          { x: 75, y: 50 },
          { x: 90, y: 60 },
          { x: 85, y: 75 },
          { x: 65, y: 75 },
          { x: 60, y: 60 },
        ],
        colorHex: '#F59E0B',
        strokeWidth: 10,
      },
      {
        points: [
          { x: 20, y: 150 },
          { x: 50, y: 140 },
          { x: 80, y: 150 },
          { x: 110, y: 140 },
          { x: 140, y: 150 },
          { x: 180, y: 140 },
        ],
        colorHex: '#38BDF8',
        strokeWidth: 7,
      },
      {
        points: [
          { x: 20, y: 170 },
          { x: 60, y: 160 },
          { x: 100, y: 170 },
          { x: 140, y: 160 },
          { x: 180, y: 170 },
        ],
        colorHex: '#0284C7',
        strokeWidth: 7,
      },
    ],
    stamps: [
      { x: 160, y: 60, symbol: '🏖️', size: 26 },
      { x: 130, y: 80, symbol: '✈️', size: 22 },
    ],
  };
}

export function createSampleGraduation(): DoodleDrawing {
  return {
    bgColorHex: '#1A1A3A',
    strokes: [
      {
        points: [
          { x: 100, y: 40 },
          { x: 115, y: 80 },
          { x: 160, y: 80 },
          { x: 125, y: 105 },
          { x: 140, y: 150 },
          { x: 100, y: 125 },
          { x: 60, y: 150 },
          { x: 75, y: 105 },
          { x: 40, y: 80 },
          { x: 85, y: 80 },
          { x: 100, y: 40 },
        ],
        colorHex: '#FBBF24',
        strokeWidth: 7,
      },
    ],
    stamps: [
      { x: 100, y: 100, symbol: '🎓', size: 32 },
      { x: 50, y: 160, symbol: '🏆', size: 26 },
      { x: 150, y: 160, symbol: '🌟', size: 26 },
    ],
  };
}

export function createInitialSeedEvents(): AgendaEvent[] {
  const now = Date.now();

  // 1. Mom's birthday tomorrow at 09:00 AM
  const tomorrow = new Date(now);
  tomorrow.setDate(tomorrow.getDate() + 1);
  tomorrow.setHours(9, 0, 0, 0);

  // 2. Beach Trip in 4 days at 07:30 AM
  const beachDate = new Date(now);
  beachDate.setDate(beachDate.getDate() + 4);
  beachDate.setHours(7, 30, 0, 0);

  // 3. Goal Achievement today at 18:00
  const todayGoal = new Date(now);
  todayGoal.setHours(18, 0, 0, 0);

  return [
    {
      id: 1,
      title: 'Cumpleaños de Mamá 🎂',
      description: 'Comprar flores, pastel de fresas y cantarle las mañanitas en familia.',
      dateTimeEpochMs: tomorrow.getTime(),
      category: 'Cumpleaños',
      importance: 'Prioritaria',
      visualType: 'DOODLE',
      doodleJson: serializeDoodle(createSampleCake()),
      photoUri: '',
      aiArtStyle: 'Celebración & Acuarela',
      voiceMessageCustom:
        '¡Hola! Hoy es el cumpleaños de mamá. ¡No olvides felicitarla con todo tu amor y llevarle su pastel favorito!',
      voicePersona: 'Cálida',
      isAlarmEnabled: true,
      isCompleted: false,
      isMemoryCapsule: true,
      reflectionNote: 'Siempre alegre, la mujer más especial de mi vida.',
      creationTimestamp: now - 3000,
    },
    {
      id: 2,
      title: 'Escapada a la Playa 🏖️',
      description: 'Fin de semana de descanso frente al mar con amigos.',
      dateTimeEpochMs: beachDate.getTime(),
      category: 'Viaje',
      importance: 'Alta',
      visualType: 'DOODLE',
      doodleJson: serializeDoodle(createSampleBeach()),
      photoUri: '',
      aiArtStyle: 'Paisaje & Escapada',
      voiceMessageCustom:
        '¡Prepara las gafas de sol y el protector! Nos vamos a disfrutar del mar y la brisa.',
      voicePersona: 'Motivadora',
      isAlarmEnabled: true,
      isCompleted: false,
      isMemoryCapsule: true,
      reflectionNote: 'Un momento para desconectar de la rutina y reconectar con la naturaleza.',
      creationTimestamp: now - 2000,
    },
    {
      id: 3,
      title: 'Presentación de Gran Proyecto 🏆',
      description: 'Entrega final y celebración con el equipo de trabajo.',
      dateTimeEpochMs: todayGoal.getTime(),
      category: 'Meta Personal',
      importance: 'Alta',
      visualType: 'DOODLE',
      doodleJson: serializeDoodle(createSampleGraduation()),
      photoUri: '',
      aiArtStyle: 'Celebración & Acuarela',
      voiceMessageCustom:
        '¡Momento crucial! Tu esfuerzo de semanas da frutos hoy. ¡Confía en tu talento y brilla!',
      voicePersona: 'Ejecutiva',
      isAlarmEnabled: true,
      isCompleted: false,
      isMemoryCapsule: false,
      reflectionNote: '',
      creationTimestamp: now - 1000,
    },
  ];
}
