
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

import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor

data class TagUiState(
    val tags: List<Tag> = emptyList(),
    val selectedTag: Tag? = null,
    val isLoading: Boolean = false,
    val isSystemInDarkTheme: Boolean = false,
    val isAddTag: Boolean = false,
    val deletedTag: Tag? = null,
    val tagColors: List<TagColor> =
        listOf(
            TagColor.Blue,
            TagColor.Green,
            TagColor.Teal,
            TagColor.Orange,
            TagColor.Purple,
            TagColor.Red,
            TagColor.Yellow,
            TagColor.Brown
        ),
    val selectedTagColor: TagColor = tagColors.first(),
    val tagName: String = "",
    val tagAddedSuccessfully: Boolean = false
)
