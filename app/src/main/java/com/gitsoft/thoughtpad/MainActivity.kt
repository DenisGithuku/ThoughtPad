
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
package com.gitsoft.thoughtpad

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.gitsoft.thoughtpad.core.model.ThemeConfig
import com.gitsoft.thoughtpad.core.toga.theme.ThoughtPadTheme
import com.gitsoft.thoughtpad.widget.UpdateWidgetWorker
import com.gitsoft.thoughtpad.widget.WORK_NAME
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val mainUiState = mutableStateOf(MainUiState())

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { mainUiState.value = it }
            }
        }

        enableEdgeToEdge()

        setContent {
            val appState: AppState = rememberAppState()
            WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
                mainUiState.value.themeConfig == ThemeConfig.LIGHT

            ThoughtPadTheme(darkTheme = mainUiState.value.themeConfig == ThemeConfig.DARK) {
                MainNavGraph(appState)
            }
        }

        scheduleWidgetUpdateWorker()
    }

    private fun scheduleWidgetUpdateWorker() {
        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = PeriodicWorkRequestBuilder<UpdateWidgetWorker>(15, java.util.concurrent.TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
    }
}
