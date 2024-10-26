
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

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gitsoft.thoughtpad.feature.addnote.AddNoteRoute
import com.gitsoft.thoughtpad.feature.notelist.NoteListRoute
import com.gitsoft.thougtpad.feature.settings.SettingsRoute

@Composable
fun MainNavGraph(appState: AppState) {
    NavHost(navController = appState.navController, startDestination = appState.startDestination) {
        composable(
            route = ThoughtPadDestination.Splash.route,
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left) +
                    fadeOut()
            }
        ) {
            SplashScreen(
                onProceed = {
                    appState.navigate(
                        ThoughtPadDestination.NoteList.route,
                        ThoughtPadDestination.Splash.route,
                        inclusive = true
                    )
                }
            )
        }
        composable(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right) + fadeIn()
            },
            route = ThoughtPadDestination.NoteList.route
        ) {
            NoteListRoute(
                onCreateNewNote = { noteId ->
                    // Replace {noteId} with the actual noteId and rebuild the route
                    val route =
                        ThoughtPadDestination.AddNote(noteId ?: -1L)
                            .route
                            .replace("{noteId}", noteId?.toString() ?: "${-1L}")
                    appState.navigate(route, route, true)
                },
                onOpenSettings = {
                    appState.navigate(
                        ThoughtPadDestination.Settings.route,
                        ThoughtPadDestination.Settings.route,
                        true
                    )
                }
            )
        }

        composable(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() },
            route = ThoughtPadDestination.AddNote().route,
            arguments =
                listOf(
                    navArgument(name = "noteId") {
                        type = NavType.LongType
                        defaultValue = -1L
                    }
                )
        ) {
            AddNoteRoute(onNavigateBack = { appState.popBackStack() })
        }

        composable(
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() },
            route = ThoughtPadDestination.Settings.route
        ) {
            SettingsRoute(onNavigateBack = { appState.popBackStack() })
        }
    }
}
