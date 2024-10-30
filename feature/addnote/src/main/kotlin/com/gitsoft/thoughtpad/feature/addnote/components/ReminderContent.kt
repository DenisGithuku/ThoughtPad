
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
package com.gitsoft.thoughtpad.feature.addnote.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.addnote.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallTitle
import java.util.Calendar

@Composable
fun ReminderContent(onDateSelected: (Long?) -> Unit) {
    val inThirtyMinutes =
        Calendar.getInstance().apply {
            set(Calendar.MINUTE, this.get(Calendar.MINUTE) + 30)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

    val tommorrowAtEight =
        Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, this.get(Calendar.DAY_OF_MONTH) + 1)
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

    Column {
        TogaSmallTitle(
            text = stringResource(R.string.remind_me_later),
            modifier = Modifier.padding(16.dp)
        )
        ReminderContentItem(
            icon = R.drawable.ic_time,
            title = "In 30 minutes",
            onClick = { onDateSelected(inThirtyMinutes.timeInMillis) }
        )

        ReminderContentItem(
            icon = R.drawable.ic_time,
            title = "Tomorrow at 8:00 am",
            onClick = { onDateSelected(tommorrowAtEight.timeInMillis) }
        )
        ReminderContentItem(
            icon = R.drawable.ic_time,
            title = "Choose date and time",
            onClick = { onDateSelected(null) }
        )
    }
}

@Composable
private fun ReminderContentItem(
    @DrawableRes icon: Int,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(painter = painterResource(id = icon), contentDescription = title)
        Spacer(modifier = Modifier.width(16.dp))
        TogaMediumBody(text = title, modifier = Modifier.weight(1f))
        description?.let { TogaSmallLabel(text = it) }
    }
}
