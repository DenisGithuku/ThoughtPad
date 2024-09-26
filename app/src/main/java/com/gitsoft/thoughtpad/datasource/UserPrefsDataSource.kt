package com.gitsoft.thoughtpad.datasource

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPrefsDataSource @Inject constructor(private val prefsDataStore: DataStore<Preferences>) {

    val userPreferencesFlow: Flow<UserPreferences> = prefsDataStore.data
        .map { preferences ->
            UserPreferences(
                themeConfig = ThemeConfig.valueOf(preferences[PreferencesKeys.THEME_CONFIG] ?: ThemeConfig.SYSTEM.name)
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