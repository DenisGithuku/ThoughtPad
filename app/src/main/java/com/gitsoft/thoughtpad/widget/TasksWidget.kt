
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
package com.gitsoft.thoughtpad.widget

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.gitsoft.thoughtpad.MainActivity
import com.gitsoft.thoughtpad.R
import com.gitsoft.thoughtpad.core.model.CheckListItem
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import timber.log.Timber

private val widgetDataKey = stringPreferencesKey("widget_data")

class TasksWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val widgetData: WidgetData =
                currentState<Preferences>()[widgetDataKey]?.decodeFromString() ?: WidgetData()
            Log.d("WidgetData", widgetData.toString())
            TasksWidgetContent(widgetData = widgetData)
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        throwable.printStackTrace()
        Timber.tag("Glance Error").d(throwable.message ?: "Unknown error")
    }

    suspend fun update(context: Context, glanceId: GlanceId, widgetData: WidgetData) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[widgetDataKey] = widgetData.encodeToString()
        }
        update(context, glanceId)
    }

    @Composable
    fun TasksWidgetContent(widgetData: WidgetData) {
        LocalContext.current

        Box(
            modifier =
                GlanceModifier.fillMaxWidth().background(GlanceTheme.colors.background).clickable {
                    actionStartActivity<MainActivity>()
                }
        ) {
            Image(
                provider = ImageProvider(R.drawable.circular_background),
                contentDescription = null,
                colorFilter = ColorFilter.tint(GlanceTheme.colors.background),
                modifier = GlanceModifier.fillMaxWidth()
            )
            LazyColumn(modifier = GlanceModifier.padding(16.dp)) {
                if (widgetData.title != null) {
                    item {
                        Column {
                            Text(text = "Reminder")
                            Text(text = widgetData.title)
                        }
                    }
                }

                if (widgetData.checkListItems.isNotEmpty()) {
                    val checkList = widgetData.checkListItems
                    items(items = checkList) { checkListItem ->
                        Row(
                            modifier = GlanceModifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CheckBox(checked = checkListItem.isChecked, onCheckedChange = {})
                            Spacer(modifier = GlanceModifier.width(8.dp))
                            checkListItem.text?.let { Text(text = it) }
                        }
                    }
                }
            }
            Button(text = ">", onClick = { actionStartActivity<MainActivity>() })
        }
    }
}

@Serializable
data class WidgetData(
    val title: String? = null,
    val checkListItems: List<CheckListItem> = emptyList()
) {
    fun encodeToString(): String {
        return Json.encodeToString(this)
    }
}

fun String.decodeFromString(): WidgetData {
    return Json.decodeFromString(this)
}
