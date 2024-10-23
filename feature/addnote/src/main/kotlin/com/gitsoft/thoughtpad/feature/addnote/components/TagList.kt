package com.gitsoft.thoughtpad.feature.addnote.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.model.Tag
import com.gitsoft.thoughtpad.feature.addnote.R
import core.gitsoft.thoughtpad.core.toga.components.chips.TogaFilterChip
import core.gitsoft.thoughtpad.core.toga.components.text.TogaLargeLabel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagList(
    modifier: Modifier = Modifier,
    tags: List<Tag>,
    onDeleteTag: (Tag) -> Unit, // Handle tag deletion
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        // Display existing tags
        AnimatedContent(tags.isEmpty(), label = "Tags", transitionSpec = {
            fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
        }) { isEmpty ->
            if (isEmpty) {
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
                ) {
                    TogaLargeLabel(R.string.no_tags)
                }
            } else {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between items
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tags.forEach {
                        TogaFilterChip(
                            text = it.name ?: return@forEach,
                            onDelete = { onDeleteTag(it) },
                            color = it.color
                        )
                    }
                }
            }
        }
    }
}