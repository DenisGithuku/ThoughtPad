
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
package com.gitsoft.thoughtpad.feature.notelist.components

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody
import com.gitsoft.thoughtpad.core.toga.tags.TagInfoType
import com.gitsoft.thoughtpad.core.toga.tags.TogaInfoTag
import com.gitsoft.thoughtpad.core.toga.theme.Error
import com.gitsoft.thoughtpad.feature.notelist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteActionBottomSheet(
    modifier: Modifier = Modifier,
    noteId: Long,
    isPinned: Boolean,
    isArchived: Boolean,
    isDeleted: Boolean,
    onEdit: (Long) -> Unit,
    onTogglePin: (Long, Boolean) -> Unit,
    onToggleDelete: (Long, Boolean) -> Unit,
    onShare: (Long) -> Unit,
    onToggleArchive: (Long, Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val pinIconColor by
        animateColorAsState(
            targetValue =
                if (isPinned) {
                    MaterialTheme.colorScheme.primary
                } else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            label = "pin icon color"
        )

    ModalBottomSheet(modifier = modifier, onDismissRequest = onDismiss) {
        NoteAction(
            title = stringResource(R.string.edit),
            onClick = { onEdit(noteId) },
            icon = R.drawable.ic_edit
        )
        NoteAction(
            title = stringResource(id = if (isPinned) R.string.unpin else R.string.pin),
            onClick = { onTogglePin(noteId, !isPinned) },
            icon = if (isPinned) R.drawable.ic_pin_filled else R.drawable.ic_pin,
            color = pinIconColor
        )
        NoteAction(
            title = stringResource(id = if (isDeleted) R.string.restore else R.string.delete),
            onClick = { onToggleDelete(noteId, !isDeleted) },
            icon = R.drawable.ic_delete,
            color = if (isDeleted) MaterialTheme.colorScheme.onSurfaceVariant else Error
        )
        NoteAction(
            title = stringResource(id = if (isArchived) R.string.unarchive else R.string.archive),
            onClick = { onToggleArchive(noteId, !isArchived) },
            icon = R.drawable.ic_archive,
            color = if (isArchived) MaterialTheme.colorScheme.onSurfaceVariant else Error
        )
        NoteAction(
            title = stringResource(R.string.share),
            onClick = { onShare(noteId) },
            icon = R.drawable.ic_share
        )
    }
}

@Composable
fun NoteAction(
    title: String,
    onClick: () -> Unit,
    textTag: Int? = null,
    @DrawableRes icon: Int,
    color: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource(id = icon), contentDescription = title, tint = color)
            TogaMediumBody(modifier = Modifier.padding(start = 8.dp), text = title, color = color)
        }
        if (textTag != null) {
            TogaInfoTag(text = stringResource(textTag), tagInfoType = TagInfoType.SUCCESS)
        }
    }
}
