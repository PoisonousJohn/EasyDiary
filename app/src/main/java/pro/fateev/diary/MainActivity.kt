/*
 * Copyright 2023 Ivan Fateev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pro.fateev.diary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pro.fateev.diary.feature.diary.ui.DiaryScreen
import pro.fateev.diary.feature.diary.ui.DiaryScreenViewModel
import pro.fateev.diary.feature.diary.ui.ImagePreview
import pro.fateev.diary.feature.diary.ui.ImagePreviewViewModel
import pro.fateev.diary.feature.diary.ui.entry.DiaryEntryScreen
import pro.fateev.diary.feature.diary.ui.entry.DiaryEntryViewModel
import pro.fateev.diary.navigation.NavigationComponent
import pro.fateev.diary.navigation.NavigationControllerImpl
import pro.fateev.diary.navigation.composable
import pro.fateev.diary.ui.screen.Routes
import pro.fateev.diary.ui.screen.dashboard.DashboardScreen
import pro.fateev.diary.ui.screen.dashboard.DashboardViewModel
import pro.fateev.diary.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostState = rememberNavController()
            val controller = NavigationControllerImpl(navHostState)
            AppTheme {
                Surface(color = MaterialTheme.colors.background) {
                    NavigationComponent(
                        startRoute = Routes.Diary,
                        navigationController = controller
                    ) {

                        composable<DiaryScreenViewModel>(
                            route = Routes.Diary,
                            navigationController = controller
                        ) { _, vm ->
                            DiaryScreen(vm)
                        }
                        composable<ImagePreviewViewModel>(
                            route = Routes.ImagePreview,
                            navigationController = controller
                        ) { _, vm ->
                            ImagePreview(vm)
                        }

                        composable<DiaryEntryViewModel>(
                            route = Routes.DiaryEntry,
                            navigationController = controller
                        ) { _, vm ->
                            DiaryEntryScreen(vm)
                        }

                        composable<DashboardViewModel>(
                            route = Routes.Dashboard,
                            navigationController = controller
                        ) { _, vm ->
                            DashboardScreen(vm)
                        }
                    }
                }
            }
        }
    }
}