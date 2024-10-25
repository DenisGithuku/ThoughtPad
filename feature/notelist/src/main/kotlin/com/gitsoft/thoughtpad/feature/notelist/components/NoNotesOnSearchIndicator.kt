package com.gitsoft.thoughtpad.feature.notelist.components

import android.graphics.Paint.Align
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.notelist.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumBody

@Composable
fun NoNotesOnSearchIndicator(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        TogaMediumBody(
            text = stringResource(R.string.no_notes_on_search),
            modifier = Modifier.padding(24.dp),
            textAlign = TextAlign.Center,
            maxLines = 10,
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}