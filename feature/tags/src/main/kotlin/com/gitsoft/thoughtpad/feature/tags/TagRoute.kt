
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

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.core.model.TagColor
import com.gitsoft.thoughtpad.core.toga.components.button.TogaIconButton
import com.gitsoft.thoughtpad.core.toga.components.button.TogaPrimaryButton
import com.gitsoft.thoughtpad.core.toga.components.chips.TogaInfoChip
import com.gitsoft.thoughtpad.core.toga.components.loading.indicators.TogaFadingCirclesIndicator
import com.gitsoft.thoughtpad.core.toga.components.scaffold.TogaStandardScaffold
import com.gitsoft.thoughtpad.core.toga.components.sheets.TogaModalBottomSheet
import com.gitsoft.thoughtpad.core.toga.components.text.TogaButtonText
import com.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody
import com.gitsoft.thoughtpad.core.toga.theme.Error
import com.gitsoft.thoughtpad.feature.tags.components.EditTagContent
import com.gitsoft.thoughtpad.feature.tags.components.NewTagContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun TagRoute(viewModel: TagViewModel = koinViewModel(), onNavigateBack: () -> Unit) {
    val state by viewModel.state.collectAsState()

    TagScreen(
        state = state,
        onNavigateBack = onNavigateBack,
        onToggleAddTag = viewModel::onToggleAddTag,
        onToggleEditTag = viewModel::onToggleEditTag,
        onChangeTagColor = viewModel::onChangeTagColor,
        onChangeTagName = viewModel::onChangeTagName,
        onEditTagColor = viewModel::onEditTagColor,
        onEditTagName = viewModel::onEditTagName,
        onTagUpdated = viewModel::onUpdateTag,
        onSaveNewTag = viewModel::onSaveTag,
        onDeleteTag = viewModel::onDeleteTag,
        onResetDeletedTag = viewModel::onResetDeletedTag,
        onUndoDeleteTag = viewModel::onReInsertTag
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TagScreen(
    state: TagUiState,
    onNavigateBack: () -> Unit,
    onToggleAddTag: (Boolean) -> Unit,
    onToggleEditTag: (Tag?) -> Unit,
    onChangeTagColor: (TagColor) -> Unit,
    onChangeTagName: (String) -> Unit,
    onEditTagName: (String) -> Unit,
    onEditTagColor: (TagColor) -> Unit,
    onTagUpdated: () -> Unit,
    onSaveNewTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit,
    onResetDeletedTag: () -> Unit,
    onUndoDeleteTag: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    if (state.isAddTag) {
        TogaModalBottomSheet(
            modifier = Modifier.testTag(TestTag.TAG_SAVE_UPDATE_BOTTOM_SHEET),
            onDismissRequest = { onToggleAddTag(false) }
        ) {
            NewTagContent(
                isSystemInDarkTheme = state.isSystemInDarkTheme,
                tagNameState = state.tagName,
                selectedTagColor = state.selectedTagColor,
                tagColors = state.tagColors,
                onChangeTagName = onChangeTagName,
                onChangeTagColor = onChangeTagColor,
                onNewTagAdded = onSaveNewTag
            )
        }
    }

    if (state.selectedTag != null) {
        TogaModalBottomSheet(
            modifier = Modifier.testTag(TestTag.TAG_SAVE_UPDATE_BOTTOM_SHEET),
            onDismissRequest = { onToggleEditTag(null) }
        ) {
            EditTagContent(
                isSystemInDarkTheme = state.isSystemInDarkTheme,
                selectedTag = state.selectedTag,
                tagColors = state.tagColors,
                onEditTagColor = onEditTagColor,
                onEditTagName = onEditTagName,
                onTagUpdated = onTagUpdated
            )
        }
    }

    LaunchedEffect(state.deletedTag, snackbarHostState) {
        if (state.deletedTag != null) {
            val result =
                snackbarHostState.showSnackbar(
                    message = context.getString(R.string.tag_deleted),
                    actionLabel = context.getString(R.string.undo),
                    duration = SnackbarDuration.Long
                )

            when (result) {
                SnackbarResult.Dismissed -> onResetDeletedTag()
                SnackbarResult.ActionPerformed -> onUndoDeleteTag()
            }
        }
    }

    BackHandler(state.selectedTag != null || state.isAddTag) {
        if (state.selectedTag != null) {
            onToggleEditTag(null)
        }

        if (state.isAddTag) {
            onToggleAddTag(false)
        }
    }

    TogaStandardScaffold(
        snackbarHostState = snackbarHostState,
        title = R.string.tag_route_title,
        onNavigateBack = onNavigateBack
    ) { innerPadding ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TogaFadingCirclesIndicator()
                    TogaMediumBody(text = stringResource(id = R.string.fetching_tags))
                }
            }
            return@TogaStandardScaffold
        }
        if (state.tags.isEmpty()) {
            NoTagsContent(onAddNewTag = { onToggleAddTag(true) })
            return@TogaStandardScaffold
        }
        LazyColumn(
            modifier =
                Modifier.fillMaxSize().padding(innerPadding).padding(16.dp).testTag(TestTag.TAG_LIST),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = state.tags, key = { it.tagId }) { tag ->
                TagRow(
                    isSystemInDarkTheme = state.isSystemInDarkTheme,
                    tag = tag,
                    onEditTag = { onToggleEditTag(tag) },
                    onDeleteTag = { onDeleteTag(tag) }
                )
            }
            item {
                TogaPrimaryButton(
                    modifier = Modifier.testTag(TestTag.ADD_TAG_BTN),
                    onClick = { onToggleAddTag(true) }
                ) {
                    TogaButtonText(text = stringResource(id = R.string.new_tag))
                }
            }
        }
    }
}

@Composable
private fun TagRow(
    isSystemInDarkTheme: Boolean,
    tag: Tag,
    onEditTag: (Tag) -> Unit,
    onDeleteTag: (Tag) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().testTag(TestTag.TAG_ITEM),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        tag.name?.let { name ->
            TogaInfoChip(
                text = name,
                color = if (isSystemInDarkTheme) tag.color.darkColor else tag.color.lightColor,
                textStyle =
                    MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TogaIconButton(
                modifier = Modifier.testTag(TestTag.EDIT_TAG_BTN),
                icon = R.drawable.ic_edit,
                contentDescription = R.string.edit,
                onClick = { onEditTag(tag) }
            )
            TogaIconButton(
                modifier = Modifier.testTag(TestTag.DELETE_TAG_BTN),
                icon = R.drawable.ic_delete,
                contentDescription = R.string.edit,
                onClick = { onDeleteTag(tag) },
                tint = Error
            )
        }
    }
}

@Composable
fun NoTagsContent(onAddNewTag: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.no_content),
            contentScale = ContentScale.Crop,
            contentDescription = stringResource(id = R.string.no_tags),
            modifier = Modifier.size(200.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TogaMediumBody(
            text = stringResource(R.string.no_tags),
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center,
            maxLines = 10
        )
        TogaPrimaryButton(onClick = onAddNewTag) {
            TogaButtonText(text = stringResource(id = R.string.add_new_tag))
        }
    }
}

internal object TestTag {
    const val TAG_ITEM = "tag_item"
    const val TAG_LIST = "tag_list"
    const val ADD_TAG_BTN = "add_tag_btn"
    const val TAG_SAVE_UPDATE_BOTTOM_SHEET = "tag_save_update_bottom_sheet"
    const val EDIT_TAG_BTN = "edit_tag_btn"
    const val DELETE_TAG_BTN = "delete_tag_btn"
    const val NEW_TAG_INPUT = "new_tag_input"
    const val EDIT_TAG_INPUT = "edit_tag_input"
    const val UPDATE_TAG_INPUT = "update_tag_input"
    const val SAVE_TAG_BTN = "save_tag_btn"
    const val UPDATE_TAG_BTN = "update_tag_btn"
}
