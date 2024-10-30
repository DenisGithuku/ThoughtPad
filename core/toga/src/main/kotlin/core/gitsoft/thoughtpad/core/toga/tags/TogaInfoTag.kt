
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
package core.gitsoft.thoughtpad.core.toga.tags

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import core.gitsoft.thoughtpad.core.toga.components.text.TogaCaption
import core.gitsoft.thoughtpad.core.toga.theme.Error
import core.gitsoft.thoughtpad.core.toga.theme.Info
import core.gitsoft.thoughtpad.core.toga.theme.Success

@Composable
fun TogaInfoTag(text: String, tagInfoType: TagInfoType) {
    Box(
        modifier =
            Modifier.clip(MaterialTheme.shapes.extraLarge)
                .background(
                    color =
                        when (tagInfoType) {
                            TagInfoType.INFO -> Info.copy(alpha = 0.4f)
                            TagInfoType.SUCCESS -> Success.copy(alpha = 0.4f)
                            TagInfoType.ERROR -> Error.copy(alpha = 0.4f)
                        },
                    shape = MaterialTheme.shapes.extraLarge
                )
                .border(
                    border =
                        BorderStroke(
                            width = 0.8.dp,
                            color =
                                when (tagInfoType) {
                                    TagInfoType.INFO -> Info
                                    TagInfoType.SUCCESS -> Success
                                    TagInfoType.ERROR -> Error
                                }
                        ),
                    shape = MaterialTheme.shapes.extraLarge
                ),
        contentAlignment = Alignment.Center
    ) {
        TogaCaption(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            caption = text,
            color =
                when (tagInfoType) {
                    TagInfoType.INFO -> Info
                    TagInfoType.SUCCESS -> Success
                    TagInfoType.ERROR -> Error
                }
        )
    }
}

enum class TagInfoType {
    SUCCESS,
    ERROR,
    INFO
}
