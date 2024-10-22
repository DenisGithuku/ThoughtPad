package core.gitsoft.thoughtpad.core.toga.components.button

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import core.gitsoft.thoughtpad.core.toga.components.text.TogaDefaultText

@Composable
fun TogaPrimaryButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    shape: Shape = MaterialTheme.shapes.extraLarge,
    border: BorderStroke? = null,
    elevation: ButtonElevation = ButtonDefaults.buttonElevation(),
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        disabledContainerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
    ),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        elevation = elevation,
        shape = shape,
        border = border,
        content = content
    )
}