
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
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.notelist.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumTitle

@Composable
fun DrawerContent(
    onOpenSettings: () -> Unit,
    onOpenTags: () -> Unit,
    onOpenArchivedNotes: () -> Unit,
    onOpenTrash: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
        DrawerHeader()
        DrawerItem(
            title = R.string.archived,
            icon = R.drawable.ic_archive,
            onClick = onOpenArchivedNotes
        )
        DrawerItem(title = R.string.trash, icon = R.drawable.ic_delete, onClick = onOpenTrash)
        DrawerItem(title = R.string.settings, icon = R.drawable.ic_settings, onClick = onOpenSettings)
        DrawerItem(title = R.string.tags, icon = R.drawable.ic_tag, onClick = onOpenTags)
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_icon),
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        TogaMediumTitle(
            text = stringResource(R.string.drawer_title),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DrawerItem(@StringRes title: Int, @DrawableRes icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(title),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        TogaMediumLabel(text = stringResource(title))
    }
}
