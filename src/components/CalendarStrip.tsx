import React, { useMemo } from 'react';
import { AgendaEvent } from '../types/agenda';

interface CalendarStripProps {
  selectedDayEpoch: number | null;
  onSelectDay: (epoch: number | null) => void;
  events: AgendaEvent[];
}

interface CalendarDayItem {
  epochDayStart: number;
  dayOfWeek: string;
  dayNumber: string;
  isToday: boolean;
  hasEvent: boolean;
}

export const CalendarStrip: React.FC<CalendarStripProps> = ({
  selectedDayEpoch,
  onSelectDay,
  events,
}) => {
  const daysList = useMemo<CalendarDayItem[]>(() => {
    const list: CalendarDayItem[] = [];
    const now = new Date();
    const todayStart = new Date(now.getFullYear(), now.getMonth(), now.getDate()).getTime();

    const startCursor = new Date(todayStart);
    startCursor.setDate(startCursor.getDate() - 3);

    const dayFormatter = new Intl.DateTimeFormat('es-ES', { weekday: 'short' });

    for (let i = 0; i <= 17; i++) {
      const dayStart = new Date(
        startCursor.getFullYear(),
        startCursor.getMonth(),
        startCursor.getDate(),
        0,
        0,
        0,
        0
      ).getTime();
      const dayEnd = dayStart + 86400000;

      const hasEvent = events.some(
        (ev) => ev.dateTimeEpochMs >= dayStart && ev.dateTimeEpochMs < dayEnd
      );
      const isToday = dayStart === todayStart;
      const rawDayName = dayFormatter.format(new Date(dayStart)).replace('.', '').toUpperCase();

      list.push({
        epochDayStart: dayStart,
        dayOfWeek: rawDayName.slice(0, 3),
        dayNumber: String(new Date(dayStart).getDate()),
        isToday,
        hasEvent,
      });

      startCursor.setDate(startCursor.getDate() + 1);
    }

    return list;
  }, [events]);

  return (
    <div className="w-full space-y-2.5">
      <div className="flex items-center justify-between px-1">
        <span className="text-sm font-bold text-slate-100">Calendario Interactivo</span>
        <button
          type="button"
          data-testid="filter_all_days"
          onClick={() => onSelectDay(null)}
          className={`px-3 py-1 rounded-full text-xs font-bold transition-colors ${
            selectedDayEpoch === null
              ? 'bg-indigo-400 text-slate-950'
              : 'bg-[#222040] text-slate-300 hover:bg-slate-700'
          }`}
        >
          Todos
        </button>
      </div>

      <div className="flex items-center gap-2 overflow-x-auto no-scrollbar pb-1">
        {daysList.map((item) => {
          const isSelected = selectedDayEpoch !== null && selectedDayEpoch === item.epochDayStart;
          return (
            <button
              key={item.epochDayStart}
              type="button"
              data-testid={`day_chip_${item.dayNumber}`}
              onClick={() => {
                if (isSelected) {
                  onSelectDay(null);
                } else {
                  onSelectDay(item.epochDayStart);
                }
              }}
              className={`w-[52px] py-2.5 rounded-2xl flex flex-col items-center justify-center shrink-0 transition-all ${
                isSelected
                  ? 'bg-indigo-400 text-slate-950 shadow-sm'
                  : item.isToday
                  ? 'bg-indigo-900/60 border-[1.5px] border-indigo-400 text-slate-100'
                  : 'bg-[#222040]/50 text-slate-300 hover:bg-[#222040]'
              }`}
            >
              <span
                className={`text-[11px] font-medium ${
                  isSelected ? 'text-slate-950' : 'text-slate-400'
                }`}
              >
                {item.dayOfWeek}
              </span>
              <span
                className={`text-base font-bold mt-0.5 tabular-nums ${
                  isSelected ? 'text-slate-950' : 'text-slate-100'
                }`}
              >
                {item.dayNumber}
              </span>
              <div className="mt-1 h-1.5 flex items-center justify-center">
                {item.hasEvent && (
                  <span
                    className={`w-1.5 h-1.5 rounded-full ${
                      isSelected ? 'bg-slate-950' : 'bg-indigo-400'
                    }`}
                  />
                )}
              </div>
            </button>
          );
        })}
      </div>
    </div>
  );
};
