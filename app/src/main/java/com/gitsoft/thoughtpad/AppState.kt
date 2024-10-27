
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

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Stable
data class AppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState
) {
    fun navigate(route: String, popupTo: String? = null, inclusive: Boolean = false) {
        navController.navigate(route) {
            if (popupTo != null) {
                this.popUpTo(popupTo) { this.inclusive = inclusive }
            } else {
                navController.popBackStack()
            }
        }
    }

    val startDestination: String
        get() = ThoughtPadDestination.Splash.route

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    fun popBackStack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
): AppState =
    remember(navController) {
        AppState(navController = navController, snackbarHostState = snackbarHostState)
    }

open class ThoughtPadDestination(val route: String) {
    data object NoteList : ThoughtPadDestination(route = "notelist")

    data class AddNote(val noteId: Long = -1L) : ThoughtPadDestination(route = "addnote/{noteId}")

    data object Splash : ThoughtPadDestination(route = "splash")

    data object Settings : ThoughtPadDestination(route = "settings")

    data object Tags : ThoughtPadDestination(route = "tags")
}
