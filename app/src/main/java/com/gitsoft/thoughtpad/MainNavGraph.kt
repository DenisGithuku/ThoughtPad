
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
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.gitsoft.thoughtpad.feature.addnote.AddNoteRoute
import com.gitsoft.thoughtpad.feature.notedetail.NoteDetailRoute
import com.gitsoft.thoughtpad.feature.notelist.NoteListRoute

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
                onOpenNoteDetail = {
                    appState.navigate(
                        ThoughtPadDestination.NoteDetail.route,
                        ThoughtPadDestination.NoteDetail.route,
                        true
                    )
                },
                onCreateNewNote = {
                    appState.navigate(
                        ThoughtPadDestination.AddNote.route,
                        ThoughtPadDestination.AddNote.route,
                        true
                    )
                }
            )
        }

        composable(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right) +
                    fadeOut()
            },
            route = ThoughtPadDestination.NoteDetail.route
        ) {
            NoteDetailRoute(onNavigateBack = { appState.popBackStack() })
        }

        composable(
            enterTransition = {
                slideIntoContainer(towards = AnimatedContentTransitionScope.SlideDirection.Right) + fadeIn()
            },
            exitTransition = {
                slideOutOfContainer(towards = AnimatedContentTransitionScope.SlideDirection.Left) +
                    fadeOut()
            },
            route = ThoughtPadDestination.AddNote.route
        ) {
            AddNoteRoute(onNavigateBack = { appState.popBackStack() })
        }
    }
}
