package com.gitsoft.thoughtpad.feature.notelist.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaSmallTitle

@Composable
fun SectionSeparator(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
) {
    TogaMediumLabel(
        text = stringResource(id = title),
        modifier = modifier
    )
}