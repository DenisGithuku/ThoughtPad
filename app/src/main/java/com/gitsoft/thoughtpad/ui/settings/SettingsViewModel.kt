package com.gitsoft.thoughtpad.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val selectedTheme: ThemeConfig = ThemeConfig.SYSTEM, val isThemeDialogShown: Boolean = false,
    val availableThemes: List<ThemeConfig> =
        listOf(ThemeConfig.SYSTEM, ThemeConfig.LIGHT, ThemeConfig.DARK),
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())

    val state = combine(
        _state, userPrefsRepository.userPreferencesFlow
    ) { state, userPrefs ->
        SettingsUiState(
            selectedTheme = userPrefs.themeConfig,
            isThemeDialogShown = state.isThemeDialogShown
        )
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = SettingsUiState()
        )

    fun onToggleTheme(themeConfig: ThemeConfig) {
        viewModelScope.launch {
            userPrefsRepository.updateThemeConfig(themeConfig)
        }
    }

    fun onToggleThemeDialog(isVisible: Boolean) {
        _state.value = _state.value.copy(isThemeDialogShown = isVisible)
    }
}