
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
package com.gitsoft.thoughtpad.feature.tags

import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import core.gitsoft.thoughtpad.core.data.repository.NotesRepository
import core.gitsoft.thoughtpad.core.data.repository.UserPrefsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TagViewModel(
    private val notesRepository: NotesRepository,
    private val userPrefsRepository: UserPrefsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<TagUiState> = MutableStateFlow(TagUiState())

    val state: StateFlow<TagUiState> =
        combine(_state, notesRepository.allTags, userPrefsRepository.userPrefs) { state, tags, prefs ->
                state.copy(
                    tags = tags,
                    isSystemInDarkTheme = prefs.themeConfig == ThemeConfig.DARK,
                    isLoading = false
                )
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TagUiState(isLoading = true))

    fun onChangeTagColor(color: TagColor) {
        _state.update { it.copy(selectedTagColor = color) }
    }

    fun onChangeTagName(value: String) {
        _state.update { it.copy(tagName = value) }
    }

    fun onSaveTag(tag: Tag) {
        viewModelScope.launch {
            val tagId = notesRepository.insertTag(tag)
            _state.update {
                it.copy(
                    tagName = "",
                    tagAddedSuccessfully = tagId != -1L,
                    isAddTag = false,
                    selectedTagColor = it.tagColors.first()
                )
            }
        }
    }

    fun onReInsertTag() {
        viewModelScope.launch {
            _state.value.deletedTag?.let { tag -> notesRepository.insertTag(tag) }
            _state.update { it.copy(deletedTag = null) }
        }
    }

    fun onDeleteTag(tag: Tag) {
        viewModelScope.launch {
            notesRepository.deleteTag(tag)
            _state.update { it.copy(deletedTag = tag) }
        }
    }

    fun onResetDeletedTag() {
        _state.update { it.copy(deletedTag = null) }
    }

    fun onUpdateTag() {
        viewModelScope.launch {
            notesRepository.updateTag(
                _state.value.selectedTag?.copy(
                    name = _state.value.selectedTag?.name?.trim()?.capitalize(Locale.current)
                ) ?: return@launch
            )
            _state.update { it.copy(selectedTag = null, selectedTagColor = it.tagColors.first()) }
        }
    }

    fun onToggleAddTag(isVisible: Boolean) {
        _state.update { it.copy(isAddTag = isVisible) }
    }

    fun onToggleEditTag(selectedTag: Tag?) {
        _state.update { it.copy(selectedTag = selectedTag) }
    }

    fun onEditTagColor(tagColor: TagColor) {
        _state.update { it.copy(selectedTag = it.selectedTag?.copy(color = tagColor)) }
    }

    fun onEditTagName(value: String) {
        _state.update {
            it.copy(selectedTag = it.selectedTag?.copy(name = value.trim().capitalize(Locale.current)))
        }
    }

    override fun onCleared() {
        super.onCleared()
        _state.update { TagUiState() }
    }
}
