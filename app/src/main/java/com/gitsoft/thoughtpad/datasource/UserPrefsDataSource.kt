
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
package com.gitsoft.thoughtpad.datasource

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.model.UserPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPrefsDataSource @Inject constructor(private val prefsDataStore: DataStore<Preferences>) {

    val userPreferencesFlow: Flow<UserPreferences> =
        prefsDataStore.data.map { preferences ->
            UserPreferences(
                themeConfig =
                    ThemeConfig.valueOf(preferences[PreferencesKeys.THEME_CONFIG] ?: ThemeConfig.SYSTEM.name)
            )
        }

    // Function to save theme preference
    suspend fun saveThemePreference(themeConfig: ThemeConfig) {
        prefsDataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_CONFIG] = themeConfig.name
        }
    }

    private object PreferencesKeys {
        val THEME_CONFIG = stringPreferencesKey("theme_config")
    }
}
