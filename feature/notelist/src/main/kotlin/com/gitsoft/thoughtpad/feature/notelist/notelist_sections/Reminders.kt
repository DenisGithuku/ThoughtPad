
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.gitsoft.thoughtpad.feature.notelist.notelist_sections

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import com.gitsoft.thoughtpad.core.model.Note
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import com.gitsoft.thoughtpad.feature.notelist.R
import com.gitsoft.thoughtpad.feature.notelist.TestTags
import com.gitsoft.thoughtpad.feature.notelist.components.NoNotesInCategoryIndicator
import com.gitsoft.thoughtpad.feature.notelist.components.NoteItemCard
import com.gitsoft.thoughtpad.feature.notelist.components.animateNoteItemCard
import com.kizitonwose.calendar.compose.CalendarLayoutInfo
import com.kizitonwose.calendar.compose.CalendarState
import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.core.CalendarMonth
import com.kizitonwose.calendar.core.OutDateStyle
import com.kizitonwose.calendar.core.daysOfWeek
import com.kizitonwose.calendar.core.firstDayOfWeekFromLocale
import com.kizitonwose.calendar.core.nextMonth
import com.kizitonwose.calendar.core.previousMonth
import java.time.DayOfWeek
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Locale
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
fun LazyStaggeredGridScope.reminders(
    reminders: List<DataWithNotesCheckListItemsAndTags>,
    reminderDisplayStyle: ReminderDisplayStyle,
    isDarkTheme: Boolean,
    selectedNote: Note? = null,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onCreateNewNote: (Long?) -> Unit,
    onToggleSelectedNote: (Note) -> Unit,
    onToggleFilterDialog: (Boolean) -> Unit
) {
    if (reminders.isEmpty()) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                NoNotesInCategoryIndicator(
                    modifier = Modifier.fillMaxSize(),
                    category = R.string.no_reminders
                )
            }
        }
    } else {
        when (reminderDisplayStyle) {
            ReminderDisplayStyle.LIST -> {
                this.items(items = reminders, key = { it.note.noteId }) { noteData ->
                    NoteItemCard(
                        modifier = Modifier.then(animateNoteItemCard()).testTag(TestTags.NOTE_ITEM_CARD),
                        isDarkTheme = isDarkTheme,
                        isSelected = selectedNote?.noteId == noteData.note.noteId,
                        noteData = noteData,
                        onClick = { onCreateNewNote(noteData.note.noteId) },
                        sharedTransitionScope = sharedTransitionScope,
                        animatedContentScope = animatedContentScope,
                        onLongClick = {
                            onToggleSelectedNote(noteData.note)
                            onToggleFilterDialog(true)
                        }
                    )
                }
            }
            ReminderDisplayStyle.CALENDAR -> {
                item(span = StaggeredGridItemSpan.FullLine) {
                    CalendarComponent(reminders = reminders, onSelectNote = { onCreateNewNote(it) })
                }
            }
        }
    }
}

@Composable
fun CalendarComponent(
    reminders: List<DataWithNotesCheckListItemsAndTags>,
    onSelectNote: (Long) -> Unit
) {
    val currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(1) } // Adjust as needed
    val endMonth = remember { currentMonth.plusMonths(2) } // Adjust as needed
    val firstDayOfWeek = remember {
        firstDayOfWeekFromLocale(Locale.getDefault())
    } // Available from the library
    val coroutineScope = rememberCoroutineScope()
    var selection = remember { mutableStateOf<CalendarDay?>(null) }
    val daysOfWeek = remember { daysOfWeek() }

    val state =
        rememberCalendarState(
            startMonth = startMonth,
            endMonth = endMonth,
            firstVisibleMonth = currentMonth,
            firstDayOfWeek = daysOfWeek.first(),
            outDateStyle = OutDateStyle.EndOfGrid
        )
    val visibleMonth = rememberFirstCompletelyVisibleMonth(state)
    LaunchedEffect(visibleMonth) {
        // Clear selection if we scroll to a new month.
        selection.value = null
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        SimpleCalendarTitle(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp),
            currentMonth = visibleMonth.yearMonth,
            goToPrevious = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.previousMonth)
                }
            },
            goToNext = {
                coroutineScope.launch {
                    state.animateScrollToMonth(state.firstVisibleMonth.yearMonth.nextMonth)
                }
            }
        )

        HorizontalCalendar(
            state = state,
            monthHeader = { MonthHeader(daysOfWeek = daysOfWeek) },
            dayContent = { calendarDay ->
                val reminderDates =
                    reminders
                        .map {
                            it.note.reminderTime?.let { Calendar.getInstance().apply { timeInMillis = it } }
                        }
                        .mapNotNull { it?.get(Calendar.DAY_OF_YEAR) }
                val isReminder = calendarDay.date.dayOfYear in reminderDates

                val id =
                    reminders
                        .find { noteData ->
                            val date =
                                Calendar.getInstance()
                                    .apply { timeInMillis = noteData.note.reminderTime ?: return@apply }
                                    .get(Calendar.DAY_OF_YEAR)
                            calendarDay.date.dayOfYear == date
                        }
                        .let { it?.note?.noteId }

                Day(
                    isToday = calendarDay.date.dayOfYear == Calendar.getInstance().get(Calendar.DAY_OF_YEAR),
                    isReminder = isReminder,
                    day = calendarDay,
                    onSelectNote = { if (id != null) onSelectNote(id) }
                )
            }
        )
    }
}

@Composable
fun Day(isToday: Boolean, isReminder: Boolean, day: CalendarDay, onSelectNote: () -> Unit) {
    Box(
        modifier =
            Modifier.aspectRatio(1f) // This is important for square sizing!
                .clip(CircleShape)
                .border(
                    shape = CircleShape,
                    color =
                        if (isToday) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Transparent
                        },
                    width = 1.dp
                )
                .background(
                    if (isReminder) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.background
                    },
                    shape = CircleShape
                )
                .clickable(enabled = isReminder, onClick = onSelectNote),
        contentAlignment = Alignment.Center
    ) {
        TogaSmallBody(
            text = day.date.dayOfMonth.toString(),
            color =
                if (isReminder) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onBackground
                }
        )
    }
}

@Composable
private fun MonthHeader(modifier: Modifier = Modifier, daysOfWeek: List<DayOfWeek> = emptyList()) {
    Row(modifier.fillMaxWidth()) {
        for (dayOfWeek in daysOfWeek) {
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary,
                text = dayOfWeek.displayText(uppercase = true),
                fontWeight = FontWeight.Light
            )
        }
    }
}

@Composable
fun SimpleCalendarTitle(
    modifier: Modifier,
    currentMonth: YearMonth,
    isHorizontal: Boolean = true,
    goToPrevious: () -> Unit,
    goToNext: () -> Unit
) {
    Row(modifier = modifier.height(40.dp), verticalAlignment = Alignment.CenterVertically) {
        CalendarNavigationIcon(
            icon = R.drawable.ic_chevron_left,
            contentDescription = "Previous",
            onClick = goToPrevious,
            isHorizontal = isHorizontal
        )
        Text(
            modifier = Modifier.weight(1f).testTag("MonthTitle"),
            text = currentMonth.displayText(),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
        CalendarNavigationIcon(
            icon = R.drawable.ic_chevron_right,
            contentDescription = "Next",
            onClick = goToNext,
            isHorizontal = isHorizontal
        )
    }
}

@Composable
private fun CalendarNavigationIcon(
    @DrawableRes icon: Int,
    contentDescription: String,
    isHorizontal: Boolean = true,
    onClick: () -> Unit
) =
    Box(
        modifier =
            Modifier.fillMaxHeight()
                .aspectRatio(1f)
                .clip(shape = CircleShape)
                .clickable(role = Role.Button, onClick = onClick)
    ) {
        val rotation =
            animateFloatAsState(
                targetValue = if (isHorizontal) 0f else 90f,
                label = "CalendarNavigationIconAnimation"
            )
        Icon(
            modifier =
                Modifier.fillMaxSize().padding(4.dp).align(Alignment.Center).rotate(rotation.value),
            painter = painterResource(icon),
            contentDescription = contentDescription
        )
    }

fun YearMonth.displayText(short: Boolean = false): String {
    return "${this.month.displayText(short = short)} ${this.year}"
}

fun Month.displayText(short: Boolean = true): String {
    val style = if (short) TextStyle.SHORT else TextStyle.FULL
    return getDisplayName(style, Locale.ENGLISH)
}

fun DayOfWeek.displayText(uppercase: Boolean = false, narrow: Boolean = false): String {
    val style = if (narrow) TextStyle.NARROW else TextStyle.SHORT
    return getDisplayName(style, Locale.ENGLISH).let { value ->
        if (uppercase) value.uppercase(Locale.ENGLISH) else value
    }
}

/**
 * Alternative way to find the first fully visible month in the layout.
 *
 * @see [rememberFirstVisibleMonthAfterScroll]
 * @see [rememberFirstMostVisibleMonth]
 */
@Composable
fun rememberFirstCompletelyVisibleMonth(state: CalendarState): CalendarMonth {
    val visibleMonth = remember(state) { mutableStateOf(state.firstVisibleMonth) }
    // Only take non-null values as null will be produced when the
    // list is mid-scroll as no index will be completely visible.
    LaunchedEffect(state) {
        snapshotFlow { state.layoutInfo.completelyVisibleMonths.firstOrNull() }
            .filterNotNull()
            .collect { month -> visibleMonth.value = month }
    }
    return visibleMonth.value
}

private val CalendarLayoutInfo.completelyVisibleMonths: List<CalendarMonth>
    get() {
        val visibleItemsInfo = this.visibleMonthsInfo.toMutableList()
        return if (visibleItemsInfo.isEmpty()) {
            emptyList()
        } else {
            val lastItem = visibleItemsInfo.last()
            val viewportSize = this.viewportEndOffset + this.viewportStartOffset
            if (lastItem.offset + lastItem.size > viewportSize) {
                visibleItemsInfo.removeAt(visibleItemsInfo.size - 1)
            }
            val firstItem = visibleItemsInfo.firstOrNull()
            if (firstItem != null && firstItem.offset < this.viewportStartOffset) {
                visibleItemsInfo.removeAt(0)
            }
            visibleItemsInfo.map { it.month }
        }
    }
