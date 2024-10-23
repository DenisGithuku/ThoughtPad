package core.gitsoft.thoughtpad.core.toga.components.chips

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel

@Composable
fun TogaInputChip(
    modifier: Modifier = Modifier,
    text: String,
    isSelected: Boolean,
    onSelectChanged: (Boolean) -> Unit,
    color: Long? = null
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                if (color != null) {
                    Color(color.toULong() shl 32)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
            .clickable(onClick = { onSelectChanged(!isSelected) }),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TogaMediumLabel(
                text = text,
                color = MaterialTheme.colorScheme.onSurface,
            )
            AnimatedVisibility(
                visible = isSelected,
                enter = scaleIn(initialScale = 0.5f) + fadeIn(initialAlpha = 0.3f),
                exit = scaleOut(targetScale = 0.5f) + fadeOut(targetAlpha = 0.3f)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun TogaFilterChipPrev() {
    TogaInputChip(text = "Work", isSelected = true, onSelectChanged = {}, color = 3326114816)
}