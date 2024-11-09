
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
package com.gitsoft.thoughtpad.feature.settings.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallLabel
import com.gitsoft.thoughtpad.feature.settings.TestTags

@Composable
fun SettingListItem(
    modifier: Modifier = Modifier,
    @DrawableRes leading: Int? = null,
    @StringRes title: Int,
    @StringRes description: Int? = null,
    @DrawableRes trailing: Int? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable(enabled = onClick != null) { onClick?.let { it() } }
                .padding(16.dp)
                .testTag(TestTags.SETTING_LIST_ITEM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            Icon(painter = painterResource(id = leading), contentDescription = stringResource(id = title))
        }
        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            TogaSmallBody(text = stringResource(id = title))
            if (description != null) {
                TogaSmallLabel(text = stringResource(id = description))
            }
        }
        if (trailing != null) {
            Icon(
                painter = painterResource(id = trailing),
                contentDescription = stringResource(id = title)
            )
        }
    }
}

@Composable
fun ToggleableSettingItem(
    modifier: Modifier = Modifier,
    @DrawableRes leading: Int? = null,
    @StringRes title: Int,
    @StringRes description: Int,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onCheckedChange(!isChecked) }
                .padding(16.dp)
                .testTag(TestTags.TOGGLEABLE_SETTING_ITEM),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leading != null) {
            Icon(painter = painterResource(id = leading), contentDescription = stringResource(id = title))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            TogaSmallBody(text = stringResource(id = title))
            TogaSmallLabel(text = stringResource(id = description))
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.padding(start = 16.dp).testTag(TestTags.TOGGLEABLE_SETTING_ITEM_SWITCH)
        )
    }
}
