
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
package com.gitsoft.thoughtpad.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.gitsoft.thoughtpad.R
import com.gitsoft.thoughtpad.model.ThemeConfig
import com.gitsoft.thoughtpad.ui.theme.ThoughtPadTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Find ComposeView and set content
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            val state by viewModel.state.collectAsState()
            ThoughtPadTheme(darkTheme = state.selectedTheme == ThemeConfig.DARK) {
                SettingsScreen(
                    state = state,
                    onToggleTheme = { viewModel.onToggleTheme(it) },
                    onToggleThemeDialog = viewModel::onToggleThemeDialog
                )
            }
        }
    }
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onToggleTheme: (ThemeConfig) -> Unit,
    onToggleThemeDialog: (Boolean) -> Unit
) {
    Scaffold { innerPadding ->
        if (state.isThemeDialogShown) {
            CLibAlertContentDialog(
                title = { Text(text = "Select theme", style = MaterialTheme.typography.headlineSmall) },
                content = {
                    Column {
                        state.availableThemes.forEach { availableTheme ->
                            Row(
                                modifier = Modifier.clickable { onToggleTheme(availableTheme) }.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                RadioButton(
                                    selected = availableTheme == state.selectedTheme,
                                    onClick = { onToggleTheme(availableTheme) }
                                )
                                Text(
                                    text = availableTheme.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                },
                onDismissRequest = { onToggleThemeDialog(false) }
            )
        }
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            SettingsItem(
                leading = {
                    Icon(
                        imageVector =
                            if (state.selectedTheme == ThemeConfig.DARK) {
                                Icons.Filled.DarkMode
                            } else Icons.Filled.LightMode,
                        contentDescription = "Theme"
                    )
                },
                title = {
                    Text(
                        text = stringResource(R.string.theme_title),
                        style =
                            MaterialTheme.typography.titleSmall.copy(color = MaterialTheme.colorScheme.onSurface)
                    )
                },
                description = {
                    Text(
                        text =
                            stringResource(
                                when (state.selectedTheme) {
                                    ThemeConfig.SYSTEM -> R.string.theme_system
                                    ThemeConfig.LIGHT -> R.string.theme_light
                                    ThemeConfig.DARK -> R.string.theme_dark
                                }
                            ),
                        style =
                            MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )
                },
                onClick = { onToggleThemeDialog(true) }
            )
        }
    }
}

@Composable
fun SettingsItem(
    leading: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    description: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier =
            Modifier.fillMaxWidth()
                .clickable(enabled = onClick != null) { onClick?.let { it() } }
                .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (leading != null) leading()
            Column {
                title()
                if (description != null) description()
            }
        }
        if (trailing != null) trailing()
    }
}

@Composable
fun CLibAlertContentDialog(
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    onDismissRequest: () -> Unit
) {
    val animateTrigger = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { launch { animateTrigger.value = true } }
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = {
            scope.launch {
                animateTrigger.value = false
                delay(100)
                onDismissRequest()
            }
        }
    ) {
        AnimatedScaleInTransition(visible = animateTrigger.value) {
            Box(
                modifier =
                    Modifier.fillMaxWidth(fraction = 0.8f)
                        .wrapContentHeight()
                        .background(
                            color = AlertDialogDefaults.containerColor,
                            shape = MaterialTheme.shapes.large
                        )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (icon != null) {
                        icon()
                    }
                    title()
                    content()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = onDismissRequest) {
                            Text(text = stringResource(id = R.string.confirm))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedScaleInTransition(
    visible: Boolean,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(animationSpec = tween(100, easing = EaseIn)) + fadeIn(),
        exit = scaleOut(animationSpec = tween(100, easing = LinearEasing)) + fadeOut(),
        content = content
    )
}

private fun setSystemTheme(uiState: SettingsUiState) {
    when {
        uiState.selectedTheme == ThemeConfig.SYSTEM -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }
}
