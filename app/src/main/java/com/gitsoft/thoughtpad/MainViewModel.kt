
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
package com.gitsoft.thoughtpad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MainUiState(val themeConfig: ThemeConfig = ThemeConfig.SYSTEM)

class MainViewModel(
    userPrefsRepository: UserPrefsRepository,
    private val notesRepository: NotesRepository
) : ViewModel() {

    val uiState =
        userPrefsRepository.userPrefs
            .mapLatest { MainUiState(it.themeConfig) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = MainUiState()
            )

    init {
        checkTags()
    }

    private fun checkTags() {
        viewModelScope.launch {
            notesRepository.allTags.collectLatest {
                if (it.isEmpty()) {
                    val defaultTags =
                        listOf(
                            Tag(name = "Work", color = TagColor.Blue), // Blue
                            Tag(name = "Personal", color = TagColor.Green), // Green
                            Tag(name = "Urgent", color = TagColor.Orange) // Orange
                        )

                    notesRepository.insertTags(defaultTags)
                }
            }
        }
    }
}
