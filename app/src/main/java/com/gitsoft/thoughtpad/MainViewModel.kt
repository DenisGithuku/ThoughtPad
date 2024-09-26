package com.gitsoft.thoughtpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.repository.NotesRepository
import com.gitsoft.thoughtpad.repository.UserPrefsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class MainUiState(
    val themeConfig: ThemeConfig = ThemeConfig.SYSTEM
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    val uiState = userPrefsRepository.userPreferencesFlow.mapLatest {
        MainUiState(it.themeConfig)
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainUiState()
        )

}