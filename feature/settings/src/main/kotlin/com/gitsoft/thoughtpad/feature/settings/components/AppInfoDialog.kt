
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

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.components.button.TogaTextButton
import com.gitsoft.thoughtpad.core.toga.components.dialog.TogaBasicDialog
import com.gitsoft.thoughtpad.core.toga.components.text.TogaCaption
import com.gitsoft.thoughtpad.core.toga.components.text.TogaSmallBody
import com.gitsoft.thoughtpad.feature.settings.BuildConfig
import com.gitsoft.thoughtpad.feature.settings.R

@Composable
fun AppInfoDialog(onDismissRequest: () -> Unit, onBuyCoffee: () -> Unit = {}) {
    TogaBasicDialog(onDismissRequest = onDismissRequest) {
        Box(
            modifier =
                Modifier.fillMaxWidth(0.8f)
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.extraLarge
                    )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier =
                    Modifier.scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)
                        .padding(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                TogaCaption(caption = "Version: ${BuildConfig.APP_VERSION}")
                TogaSmallBody(
                    text = stringResource(R.string.about_app_title),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                TogaSmallBody(text = stringResource(R.string.about_app), textAlign = TextAlign.Center)
                TogaSmallBody(
                    text = stringResource(R.string.about_developer_title),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                TogaSmallBody(text = stringResource(R.string.about_developer), textAlign = TextAlign.Center)
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.buy_me_a_coffee_image),
                    contentDescription = stringResource(R.string.buy_me_a_coffee),
                    modifier = Modifier.size(width = 100.dp, height = 56.dp).clickable { onBuyCoffee() }
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TogaTextButton(text = R.string.confirm, onClick = onDismissRequest)
                }
            }
        }
    }
}
