package core.gitsoft.thoughtpad.core.toga.components.chips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel

@Composable
fun TogaInfoChip(
    modifier: Modifier = Modifier,
    text: String,
    color: Long? = null,
) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.extraLarge)
            .background(Color(color ?: 0xFFE0E0E0))
    ) {
        TogaMediumLabel(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
        )

    }
}