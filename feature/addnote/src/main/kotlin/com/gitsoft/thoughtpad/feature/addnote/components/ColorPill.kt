package com.gitsoft.thoughtpad.feature.addnote.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ColorPill(color: Color, isSelected: Boolean, onSelect: (Color) -> Unit) {
    val borderColor by animateColorAsState(
        if (isSelected) {
            MaterialTheme.colorScheme.onBackground
        } else {
            Color.Transparent
        }
    )
    Box(modifier = Modifier
        .size(40.dp)
        .clip(CircleShape)
        .border(width = 3.dp, color = borderColor, shape = CircleShape)
        .background(color)
        .clickable { onSelect(color) })
}