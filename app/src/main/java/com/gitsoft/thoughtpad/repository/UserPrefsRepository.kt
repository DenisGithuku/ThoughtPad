package com.gitsoft.thoughtpad.repository

import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPrefsRepository {
    val userPreferencesFlow: Flow<UserPreferences>

    suspend fun updateThemeConfig(themeConfig: ThemeConfig)

}