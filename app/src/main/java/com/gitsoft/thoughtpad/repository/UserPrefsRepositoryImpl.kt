package com.gitsoft.thoughtpad.repository

import com.gitsoft.thoughtpad.datasource.UserPrefsDataSource
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.model.UserPreferences
import kotlinx.coroutines.flow.Flow

class UserPrefsRepositoryImpl(
    private val userPreferencesDataSource: UserPrefsDataSource,
): UserPrefsRepository {

    override val userPreferencesFlow: Flow<UserPreferences> = userPreferencesDataSource.userPreferencesFlow

    override suspend fun updateThemeConfig(themeConfig: ThemeConfig) = userPreferencesDataSource.saveThemePreference(themeConfig)
}
