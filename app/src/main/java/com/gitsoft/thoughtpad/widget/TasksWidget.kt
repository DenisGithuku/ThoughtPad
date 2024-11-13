package com.gitsoft.thoughtpad.widget

import android.content.Context
import android.util.Log
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.ButtonDefaults
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.gitsoft.thoughtpad.MainActivity
import com.gitsoft.thoughtpad.core.model.DataWithNotesCheckListItemsAndTags
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

private val tasksKey = stringPreferencesKey("tasks")

class TasksWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context, id: GlanceId
    ) {
        provideContent {
            val tasks: List<DataWithNotesCheckListItemsAndTags> =
                currentState<Preferences>()[tasksKey]?.decodeToDataList() ?: emptyList()
            TasksWidgetContent(
                tasks = tasks
            )
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        Timber.tag("Glance Error").d(throwable.message ?: "Unknown error")
    }

    suspend fun update(
        context: Context,
        glanceId: GlanceId,
        tasks: List<DataWithNotesCheckListItemsAndTags>
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[tasksKey] = tasks.encodeToString()
        }
        update(context, glanceId)
    }

    @Composable
    fun TasksWidgetContent(
        tasks: List<DataWithNotesCheckListItemsAndTags>
    ) {
        LocalContext.current

        val tasks =
            tasks.filter { it.note.reminderTime != null && it.note.reminderTime?.let { it > System.currentTimeMillis() } == true }
                .take(3)
        val checklist = tasks.filter { it.checkListItems.isNotEmpty() }.first().checkListItems
        Box(
            modifier = GlanceModifier.fillMaxSize().cornerRadius(32.dp)
                .background(GlanceTheme.colors.background)
        ) {
            LazyColumn(modifier = GlanceModifier.fillMaxSize()) {
                if (tasks.isEmpty() && checklist.isEmpty()) {
                    item {
                        Button(
                            text = "Add tasks",
                            onClick = {
                                actionStartActivity<MainActivity>()
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = GlanceTheme.colors.primary,
                                contentColor = GlanceTheme.colors.onPrimary
                            ),
                            modifier = GlanceModifier.cornerRadius(32.dp)
                        )
                    }
                }
                if (tasks.isNotEmpty()) {
                    item {
                        Text(text = "Tasks")
                    }
                    items(items = tasks) { task ->
                        Row(
                            modifier = GlanceModifier.fillMaxWidth().clickable {
                                actionStartActivity<MainActivity>()
                            }.padding(8.dp)
                        ) {
                            task.note.noteTitle?.let { Text(it) }
                        }
                    }
                }

                if (checklist.isNotEmpty()) {
                    items(items = checklist) { checkListItem ->
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(checked = checkListItem.isChecked, onCheckedChange = null)
                            Spacer(modifier = GlanceModifier.width(8.dp))
                            checkListItem.text?.let { Text(text = it) }
                        }
                    }
                }
            }
        }
    }
}

fun List<DataWithNotesCheckListItemsAndTags>.encodeToString(): String {
    return Json.encodeToString(this)
}

fun String.decodeToDataList(): List<DataWithNotesCheckListItemsAndTags> {
    return try {
        // Assuming the string is in JSON format
        Json.decodeFromString(this)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
