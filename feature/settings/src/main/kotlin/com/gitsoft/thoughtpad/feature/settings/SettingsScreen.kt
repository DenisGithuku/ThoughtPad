
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
package com.gitsoft.thoughtpad.feature.settings

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.gitsoft.thoughtpad.core.model.ReminderDisplayStyle
import com.gitsoft.thoughtpad.core.model.ReminderFrequency
import com.gitsoft.thoughtpad.core.model.SortOrder
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import com.gitsoft.thoughtpad.core.toga.components.dialog.TogaContentDialog
import com.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import com.gitsoft.thoughtpad.feature.settings.components.AppInfoDialog
import com.gitsoft.thoughtpad.feature.settings.components.SettingListItem
import com.gitsoft.thoughtpad.feature.settings.components.SettingSectionTitle
import com.gitsoft.thoughtpad.feature.settings.components.ToggleableSettingItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsRoute(onNavigateBack: () -> Unit, viewModel: SettingsViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    SettingsScreen(
        state = state,
        onToggleTheme = viewModel::onToggleTheme,
        onToggleThemeDialog = viewModel::onToggleThemeDialog,
        onNavigateBack = onNavigateBack,
        onToggleReminderDisplayStyle = viewModel::onToggleReminderDisplayStyle,
        onToggleReminderFrequencyDialog = viewModel::onToggleReminderFrequencyDialog,
        onToggleReminderFrequency = viewModel::onToggleReminderFrequency,
        onTogglePeriodicReminders = viewModel::onTogglePeriodicReminders,
        onToggleReminderStyleDialog = viewModel::onToggleReminderStyleDialog,
        onToggleSortDialog = viewModel::onToggleSortDialog,
        onToggleSortOrder = viewModel::onToggleSortOrder,
        onToggleAppInfoDialog = viewModel::onToggleAppInfoDialog
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    state: SettingsUiState,
    onToggleTheme: (ThemeConfig) -> Unit,
    onToggleThemeDialog: (Boolean) -> Unit,
    onNavigateBack: () -> Unit,
    onTogglePeriodicReminders: (Boolean) -> Unit,
    onToggleReminderStyleDialog: (Boolean) -> Unit,
    onToggleReminderDisplayStyle: (ReminderDisplayStyle) -> Unit,
    onToggleReminderFrequencyDialog: (Boolean) -> Unit,
    onToggleReminderFrequency: (ReminderFrequency) -> Unit,
    onToggleSortOrder: (SortOrder) -> Unit,
    onToggleSortDialog: (Boolean) -> Unit,
    onToggleAppInfoDialog: (Boolean) -> Unit
) {
    val context = LocalContext.current

    AnimatedVisibility(
        visible = state.isThemeDialogShown,
        enter =
            fadeIn(spring(stiffness = Spring.StiffnessHigh)) +
                scaleIn(
                    initialScale = .8f,
                    animationSpec =
                        spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessMediumLow
                        )
                ),
        exit = slideOutVertically { it / 8 } + fadeOut() + scaleOut(targetScale = .95f)
    ) {
        TogaContentDialog(
            title = { Text(text = "Select theme", style = MaterialTheme.typography.headlineSmall) },
            content = {
                Column(modifier = Modifier.testTag(TestTags.THEME_COLUMN).fillMaxWidth()) {
                    state.availableThemes.forEach { availableTheme ->
                        Row(
                            modifier =
                                Modifier.clickable(
                                        role = Role.RadioButton,
                                        enabled = true,
                                        onClick = { onToggleTheme(availableTheme) }
                                    )
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = availableTheme == state.selectedTheme,
                                onClick = { onToggleTheme(availableTheme) }
                            )
                            Text(
                                text = availableTheme.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            onDismissRequest = { onToggleThemeDialog(false) }
        )
    }

    if (state.isReminderStyleDialogShown) {
        TogaContentDialog(
            title = {
                Text(text = "Reminder display style", style = MaterialTheme.typography.headlineSmall)
            },
            content = {
                Column(modifier = Modifier.testTag(TestTags.REMINDER_STYLE_COLUMN).fillMaxWidth()) {
                    state.availableReminderDisplayStyles.forEach { availableStyle ->
                        Row(
                            modifier =
                                Modifier.clickable(
                                        role = Role.RadioButton,
                                        enabled = true,
                                        onClick = { onToggleReminderDisplayStyle(availableStyle) }
                                    )
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = availableStyle == state.reminderDisplayStyle,
                                onClick = { onToggleReminderDisplayStyle(availableStyle) }
                            )
                            Text(
                                text = availableStyle.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            onDismissRequest = { onToggleReminderStyleDialog(false) }
        )
    }

    if (state.isReminderFrequencyDialogShown) {
        TogaContentDialog(
            title = { Text(text = "Reminder frequency", style = MaterialTheme.typography.headlineSmall) },
            content = {
                Column(modifier = Modifier.testTag(TestTags.REMINDER_FREQUENCY_COLUMN).fillMaxWidth()) {
                    state.availableReminderFrequencies.forEach { availableFrequency ->
                        Row(
                            modifier =
                                Modifier.clickable(
                                        role = Role.RadioButton,
                                        enabled = true,
                                        onClick = { onToggleReminderFrequency(availableFrequency) }
                                    )
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = availableFrequency == state.reminderFrequency,
                                onClick = { onToggleReminderFrequency(availableFrequency) }
                            )
                            Text(
                                text = availableFrequency.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            onDismissRequest = { onToggleReminderFrequencyDialog(false) }
        )
    }

    if (state.isAppInfoDialogShown) {
        AppInfoDialog(
            onDismissRequest = { onToggleAppInfoDialog(false) },
            onBuyCoffee = {
                val intent =
                    Intent(Intent.ACTION_VIEW, context.getString(R.string.buy_me_a_coffee_url).toUri())
                context.startActivity(intent)
            }
        )
    }

    if (state.isSortDialogShown) {
        TogaContentDialog(
            title = { Text(text = "Sort content by", style = MaterialTheme.typography.headlineSmall) },
            content = {
                Column(modifier = Modifier.testTag(TestTags.SORT_ORDER_COLUMN).fillMaxWidth()) {
                    state.availableSortOrders.forEach { sortType ->
                        Row(
                            modifier =
                                Modifier.clickable(
                                        role = Role.RadioButton,
                                        enabled = true,
                                        onClick = { onToggleSortOrder(sortType) }
                                    )
                                    .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = sortType == state.sortOrder,
                                onClick = { onToggleSortOrder(sortType) }
                            )
                            Text(
                                text = sortType.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            },
            onDismissRequest = { onToggleSortDialog(false) }
        )
    }

    TogaStandardScaffold(title = R.string.settings, onNavigateBack = onNavigateBack) { innerPadding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding).animateContentSize()) {
            item { SettingSectionTitle(modifier = Modifier.animateItem(), title = R.string.appearance) }
            item {
                SettingListItem(
                    modifier = Modifier.animateItem(),
                    leading =
                        if (state.selectedTheme == ThemeConfig.DARK) {
                            R.drawable.ic_dark_mode
                        } else R.drawable.ic_light_mode,
                    title = R.string.theme_title,
                    description =
                        when (state.selectedTheme) {
                            ThemeConfig.LIGHT -> R.string.theme_light
                            ThemeConfig.DARK -> R.string.theme_dark
                        },
                    onClick = { onToggleThemeDialog(true) },
                    trailing = R.drawable.ic_chevron_right
                )
            }
            item {
                SettingListItem(
                    modifier = Modifier.animateItem(),
                    leading = R.drawable.ic_reminder_alert,
                    title = R.string.reminder_display_style_title,
                    description =
                        when (state.reminderDisplayStyle) {
                            ReminderDisplayStyle.LIST -> R.string.reminder_display_style_list
                            ReminderDisplayStyle.CALENDAR -> R.string.reminder_display_style_calendar
                        },
                    onClick = { onToggleReminderStyleDialog(true) },
                    trailing = R.drawable.ic_chevron_right
                )
            }
            item {
                SettingListItem(
                    modifier = Modifier.animateItem(),
                    leading = R.drawable.ic_sort,
                    title = R.string.sort_title,
                    description =
                        when (state.sortOrder) {
                            SortOrder.TITLE -> R.string.sort_by_title
                            SortOrder.DATE -> R.string.sort_by_date
                        },
                    onClick = { onToggleSortDialog(true) },
                    trailing = R.drawable.ic_chevron_right
                )
            }
            item {
                SettingSectionTitle(
                    modifier = Modifier.animateItem(),
                    title = R.string.notifications_and_reminders
                )
            }
            item {
                ToggleableSettingItem(
                    modifier = Modifier.animateItem(),
                    leading = R.drawable.ic_notifications_outlined,
                    title = R.string.notifications_title,
                    description = R.string.notifications_description,
                    isChecked = state.isPeriodicRemindersEnabled,
                    onCheckedChange = onTogglePeriodicReminders
                )
            }
            item {
                if (state.isPeriodicRemindersEnabled) {
                    SettingListItem(
                        modifier = Modifier.animateItem(),
                        leading = R.drawable.ic_frequency,
                        title = R.string.reminder_frequency_title,
                        description =
                            when (state.reminderFrequency) {
                                ReminderFrequency.NEVER -> R.string.never_reminder_frequency_description
                                ReminderFrequency.DAILY -> R.string.daily_reminder_frequency_description
                                ReminderFrequency.WEEKLY -> R.string.weekly_reminder_frequency_description
                            },
                        onClick = { onToggleReminderFrequencyDialog(true) },
                        trailing = R.drawable.ic_chevron_right
                    )
                }
            }
            item {
                SettingListItem(
                    modifier = Modifier.animateItem(),
                    leading = R.drawable.ic_notification_tone,
                    title = R.string.notification_tone_title,
                    description = R.string.notification_tone_description,
                    onClick = {},
                    trailing = R.drawable.ic_chevron_right
                )
            }
            item {
                SettingSectionTitle(modifier = Modifier.animateItem(), title = R.string.help_and_support)
            }
            item {
                SettingListItem(
                    modifier = Modifier.animateItem(),
                    leading = R.drawable.ic_info_circle,
                    title = R.string.app_info,
                    onClick = { onToggleAppInfoDialog(true) }
                )
            }
        }
    }
}

internal object TestTags {
    const val SETTING_LIST_ITEM = "setting_list_item"
    const val TOGGLEABLE_SETTING_ITEM = "toggleable_setting_item"
    const val THEME_COLUMN = "theme_column"
    const val REMINDER_STYLE_COLUMN = "reminder_style_column"
    const val REMINDER_FREQUENCY_COLUMN = "reminder_frequency_column"
    const val SORT_ORDER_COLUMN = "sort_type_column"
    const val TOGGLEABLE_SETTING_ITEM_SWITCH = "toggleable_setting_item_switch"
}
