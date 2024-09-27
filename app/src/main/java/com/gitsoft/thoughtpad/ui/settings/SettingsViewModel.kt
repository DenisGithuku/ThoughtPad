
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
package com.gitsoft.thoughtpad.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val selectedTheme: ThemeConfig = ThemeConfig.SYSTEM,
    val isThemeDialogShown: Boolean = false,
    val availableThemes: List<ThemeConfig> =
        listOf(ThemeConfig.SYSTEM, ThemeConfig.LIGHT, ThemeConfig.DARK)
)

@HiltViewModel
class SettingsViewModel @Inject constructor(private val userPrefsRepository: UserPrefsRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())

    val state =
        combine(_state, userPrefsRepository.userPreferencesFlow) { state, userPrefs ->
                SettingsUiState(
                    selectedTheme = userPrefs.themeConfig,
                    isThemeDialogShown = state.isThemeDialogShown
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SettingsUiState()
            )

    fun onToggleTheme(themeConfig: ThemeConfig) {
        viewModelScope.launch { userPrefsRepository.updateThemeConfig(themeConfig) }
    }

    fun onToggleThemeDialog(isVisible: Boolean) {
        _state.value = _state.value.copy(isThemeDialogShown = isVisible)
    }
}
