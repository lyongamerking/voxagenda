package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AgendaEvent
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class CalendarDayItem(
    val epochDayStart: Long,
    val dayOfWeek: String,
    val dayNumber: String,
    val isToday: Boolean,
    val hasEvent: Boolean
)

@Composable
fun CalendarStrip(
    selectedDayEpoch: Long?, // null means "All"
    onSelectDay: (Long?) -> Unit,
    events: List<AgendaEvent>,
    modifier: Modifier = Modifier
) {
    val dayFormat = remember { SimpleDateFormat("EEE", Locale("es", "ES")) }
    val daysList = remember(events) {
        val list = mutableListOf<CalendarDayItem>()
        val cal = Calendar.getInstance()
        // Start 3 days before today, go 14 days forward
        cal.add(Calendar.DAY_OF_YEAR, -3)

        val todayCal = Calendar.getInstance()
        val todayYear = todayCal.get(Calendar.YEAR)
        val todayDayOfYear = todayCal.get(Calendar.DAY_OF_YEAR)

        for (i in 0..17) {
            val startCal = Calendar.getInstance().apply {
                timeInMillis = cal.timeInMillis
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }
            val endCal = Calendar.getInstance().apply {
                timeInMillis = startCal.timeInMillis
                add(Calendar.DAY_OF_YEAR, 1)
            }

            val hasEvent = events.any {
                it.dateTimeEpochMs >= startCal.timeInMillis && it.dateTimeEpochMs < endCal.timeInMillis
            }

            val isToday = cal.get(Calendar.YEAR) == todayYear && cal.get(Calendar.DAY_OF_YEAR) == todayDayOfYear

            list.add(
                CalendarDayItem(
                    epochDayStart = startCal.timeInMillis,
                    dayOfWeek = dayFormat.format(cal.time).uppercase(),
                    dayNumber = cal.get(Calendar.DAY_OF_MONTH).toString(),
                    isToday = isToday,
                    hasEvent = hasEvent
                )
            )
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        list
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Calendario Interactivo",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // "Ver Todos" pill
            Surface(
                shape = RoundedCornerShape(100.dp),
                color = if (selectedDayEpoch == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .clickable { onSelectDay(null) }
                    .testTag("filter_all_days")
            ) {
                Text(
                    text = "Todos",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (selectedDayEpoch == null) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(daysList) { item ->
                val isSelected = selectedDayEpoch != null && selectedDayEpoch == item.epochDayStart
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        item.isToday -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    },
                    border = if (item.isToday && !isSelected) {
                        androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                    } else null,
                    modifier = Modifier
                        .clickable {
                            if (isSelected) {
                                onSelectDay(null)
                            } else {
                                onSelectDay(item.epochDayStart)
                            }
                        }
                        .testTag("day_chip_${item.dayNumber}")
                ) {
                    Column(
                        modifier = Modifier
                            .width(52.dp)
                            .padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.dayOfWeek,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.dayNumber,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (item.hasEvent) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
                            )
                        } else {
                            Spacer(modifier = Modifier.size(6.dp))
                        }
                    }
                }
            }
        }
    }
}
