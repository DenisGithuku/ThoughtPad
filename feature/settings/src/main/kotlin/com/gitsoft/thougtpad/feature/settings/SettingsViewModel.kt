
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
package com.gitsoft.thougtpad.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPrefsRepository: UserPrefsRepository) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())

    val state: StateFlow<SettingsUiState> =
        combine(_state, userPrefsRepository.userPrefs) { state, userPrefs ->
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
        viewModelScope.launch { userPrefsRepository.updateTheme(themeConfig) }
    }

    fun onToggleThemeDialog(isVisible: Boolean) {
        _state.update { it.copy(isThemeDialogShown = isVisible) }
    }
}
