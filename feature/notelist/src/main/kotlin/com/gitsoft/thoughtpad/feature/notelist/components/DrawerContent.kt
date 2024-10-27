
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
package com.gitsoft.thoughtpad.feature.notelist.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gitsoft.thoughtpad.feature.notelist.R
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumLabel
import core.gitsoft.thoughtpad.core.toga.components.text.TogaMediumTitle

@Composable
fun DrawerContent(
    selectedDrawerItem: DrawerItem,
    onOpenDrawerItem: (DrawerItem) -> Unit,
    onNavigateToRoute: (SidebarRoute) -> Unit
) {
    val drawerItems =
        listOf(DrawerItem.All, DrawerItem.Reminders, DrawerItem.Archived, DrawerItem.Trash)
    val sideBarRoutes = listOf(SidebarRoute.Tags, SidebarRoute.Settings)

    Column(modifier = Modifier.fillMaxWidth(0.75f)) {
        DrawerHeader()
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.8.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        drawerItems.forEach {
            NavigationDrawerItem(
                selected = selectedDrawerItem == it,
                icon = {
                    Icon(
                        painter = painterResource(id = it.icon),
                        tint = MaterialTheme.colorScheme.onSurface,
                        contentDescription = stringResource(it.title)
                    )
                },
                label = { TogaMediumLabel(text = stringResource(it.title)) },
                onClick = { onOpenDrawerItem(it) },
                colors =
                    NavigationDrawerItemDefaults.colors(
                        selectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurface,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        selectedBadgeColor = MaterialTheme.colorScheme.secondary,
                        unselectedTextColor = MaterialTheme.colorScheme.surfaceVariant,
                        unselectedContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        unselectedBadgeColor = MaterialTheme.colorScheme.onSurface
                    ),
                shape = MaterialTheme.shapes.extraSmall.copy(all = CornerSize(0.dp))
            )
        }
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 0.8.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        )
        sideBarRoutes.forEach { route ->
            SideBarRouteItem(
                sidebarRoute = route,
                title = route.title,
                icon = route.icon,
                onClick = onNavigateToRoute
            )
        }
    }
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_icon),
            contentDescription = stringResource(R.string.app_icon),
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.height(16.dp))
        TogaMediumTitle(
            text = stringResource(R.string.drawer_title),
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SideBarRouteItem(
    sidebarRoute: SidebarRoute,
    @StringRes title: Int,
    @DrawableRes icon: Int,
    onClick: (SidebarRoute) -> Unit
) {
    Row(
        modifier =
            Modifier.fillMaxWidth().clickable(onClick = { onClick(sidebarRoute) }).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(title),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        TogaMediumLabel(text = stringResource(title))
    }
}

sealed class DrawerItem(@StringRes val title: Int, @DrawableRes val icon: Int) {
    data object All : DrawerItem(title = R.string.notes, icon = R.drawable.ic_bulb)

    data object Archived : DrawerItem(title = R.string.archived, icon = R.drawable.ic_archive)

    data object Reminders : DrawerItem(title = R.string.reminders, icon = R.drawable.ic_reminder)

    data object Trash : DrawerItem(title = R.string.trash, icon = R.drawable.ic_delete)
}

sealed class SidebarRoute(@StringRes val title: Int, @DrawableRes val icon: Int) {
    data object Settings : SidebarRoute(title = R.string.settings, icon = R.drawable.ic_settings)

    data object Tags : SidebarRoute(title = R.string.tags, icon = R.drawable.ic_tag)
}
