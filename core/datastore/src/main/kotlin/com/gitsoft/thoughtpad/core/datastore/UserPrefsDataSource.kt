
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
package com.gitsoft.thoughtpad.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import com.gitsoft.thoughtpad.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefsDataSource(private val preferences: DataStore<Preferences>) {

    val userPrefs: Flow<UserPreferences>
        get() =
            preferences.data.map {
                UserPreferences(
                    themeConfig =
                        ThemeConfig.valueOf(it[PreferencesKeys.THEME_CONFIG] ?: ThemeConfig.SYSTEM.name),
                    isNotificationPermissionsGranted =
                        it[PreferencesKeys.NOTIFICATION_PERMISSION]?.toBoolean() ?: false
                )
            }

    private suspend fun updateValue(key: Preferences.Key<String>, value: String) {
        preferences.updateData { it.toMutablePreferences().apply { set(key, value) } }
    }

    private suspend fun clearValue(key: Preferences.Key<String>) {
        preferences.updateData { it.toMutablePreferences().apply { remove(key) } }
    }

    suspend fun updateTheme(themeConfig: ThemeConfig) {
        updateValue(PreferencesKeys.THEME_CONFIG, themeConfig.name)
    }

    suspend fun updateNotificationPermission(isGranted: Boolean) {
        updateValue(PreferencesKeys.NOTIFICATION_PERMISSION, isGranted.toString())
    }
}

private object PreferencesKeys {
    val THEME_CONFIG = stringPreferencesKey("theme_config")
    val NOTIFICATION_PERMISSION = stringPreferencesKey("notification_permission")
}
