import { AgendaEvent } from '../types/agenda';

type VoiceStateListener = (isSpeaking: boolean, currentText: string) => void;

class VoiceAgentService {
  private isSpeaking = false;
  private currentText = '';
  private listeners: Set<VoiceStateListener> = new Set();
  private fallbackTimer: number | null = null;

  subscribe(listener: VoiceStateListener): () => void {
    this.listeners.add(listener);
    listener(this.isSpeaking, this.currentText);
    return () => {
      this.listeners.delete(listener);
    };
  }

  private notify() {
    this.listeners.forEach((l) => l(this.isSpeaking, this.currentText));
  }

  speak(text: string, persona: string = 'Cálida', onDone?: () => void) {
    if (!text || text.trim().length === 0) return;

    this.stop();

    this.isSpeaking = true;
    this.currentText = text;
    this.notify();

    let pitch = 1.0;
    let rate = 0.95;

    switch (persona) {
      case 'Motivadora':
        pitch = 1.15;
        rate = 1.05;
        break;
      case 'Ejecutiva':
        pitch = 0.92;
        rate = 1.0;
        break;
      case 'Poética':
        pitch = 0.88;
        rate = 0.85;
        break;
      default: // Cálida
        pitch = 1.0;
        rate = 0.95;
        break;
    }

    const durationEstimateMs = Math.min(Math.max(text.length * 65, 3200), 14000);

    // Fallback timeout in case browser speechSynthesis doesn't fire onend
    this.fallbackTimer = window.setTimeout(() => {
      if (this.isSpeaking) {
        this.isSpeaking = false;
        this.notify();
        onDone?.();
      }
    }, durationEstimateMs);

    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      try {
        window.speechSynthesis.cancel();
        const utterance = new SpeechSynthesisUtterance(text);
        utterance.lang = 'es-ES';
        utterance.pitch = pitch;
        utterance.rate = rate;

        const voices = window.speechSynthesis.getVoices();
        const spanishVoice =
          voices.find((v) => v.lang.toLowerCase().startsWith('es') && v.name.toLowerCase().includes('female')) ||
          voices.find((v) => v.lang.toLowerCase().startsWith('es'));
        if (spanishVoice) {
          utterance.voice = spanishVoice;
        }

        utterance.onend = () => {
          if (this.fallbackTimer) {
            clearTimeout(this.fallbackTimer);
            this.fallbackTimer = null;
          }
          this.isSpeaking = false;
          this.notify();
          onDone?.();
        };

        utterance.onerror = () => {
          // Keep visual caption running until fallbackTimer completes if speech synthesis is blocked by browser autoplay
        };

        window.speechSynthesis.speak(utterance);
      } catch {
        // Fallback timer handles visual state
      }
    }
  }

  stop() {
    if (this.fallbackTimer) {
      clearTimeout(this.fallbackTimer);
      this.fallbackTimer = null;
    }
    if (typeof window !== 'undefined' && 'speechSynthesis' in window) {
      try {
        window.speechSynthesis.cancel();
      } catch {
        // ignore
      }
    }
    this.isSpeaking = false;
    this.notify();
  }

  buildDailyBriefing(todayEvents: AgendaEvent[], upcomingEvents: AgendaEvent[]): string {
    const formatTime = (ms: number) =>
      new Date(ms).toLocaleTimeString('es-ES', { hour: 'numeric', minute: '2-digit', hour12: true });

    if (todayEvents.length === 0) {
      if (upcomingEvents.length > 0) {
        const next = upcomingEvents[0];
        const nextTime = formatTime(next.dateTimeEpochMs);
        return `¡Hola! Aura reportándose. Hoy no tienes eventos urgentes. Tu próximo evento es ${next.title} programado para las ${nextTime}. Es una gran oportunidad para relajarte o preparar tus metas.`;
      }
      return '¡Buenos días! Tu agente personal Aura te saluda. Hoy tu agenda está completamente despejada. Aprovecha el día para cuidar de ti, disfrutar y crear nuevos recuerdos.';
    }

    const count = todayEvents.length;
    const first = todayEvents[0];
    const firstTime = formatTime(first.dateTimeEpochMs);

    if (count === 1) {
      return `¡Hola! Aura aquí. Hoy tienes un evento agendado: ${first.title} a las ${firstTime}. Te deseo el mayor de los éxitos en esta actividad.`;
    } else {
      const second = todayEvents[1];
      const secondTime = formatTime(second.dateTimeEpochMs);
      return `¡Buenos días! Tu asistente Aura te informa: para el día de hoy tienes ${count} compromisos. Primero: ${first.title} a las ${firstTime}, y luego ${second.title} a las ${secondTime}. ¡A por todas con energía positiva!`;
    }
  }
}

export const voiceAgent = new VoiceAgentService();
