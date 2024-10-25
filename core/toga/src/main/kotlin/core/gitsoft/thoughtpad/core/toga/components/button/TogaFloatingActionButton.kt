package core.gitsoft.thoughtpad.core.toga.components.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.core.toga.R

@Composable
fun TogaFloatingActionButton(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes contentDescription: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onClick)
    ) {
        Icon(
            modifier = Modifier
                .padding(16.dp),
            painter = painterResource(id = icon),
            contentDescription = stringResource(id = contentDescription),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
private fun TogaFloatingActionButtonPrev() {
    TogaFloatingActionButton(
        icon = R.drawable.ic_add_circle,
        contentDescription = R.string.add_note,
        onClick = {}
    )
}