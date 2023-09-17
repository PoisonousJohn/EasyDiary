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
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pro.fateev.diary.feature.diary.ui.DiaryScreen
import pro.fateev.diary.feature.diary.ui.DiaryScreenViewModel
import pro.fateev.diary.feature.diary.ui.ImagePreview
import pro.fateev.diary.feature.diary.ui.ImagePreviewViewModel
import pro.fateev.diary.feature.diary.ui.YesNoScreen
import pro.fateev.diary.feature.diary.ui.entry.DiaryEntryScreen
import pro.fateev.diary.feature.diary.ui.entry.DiaryEntryViewModel
import pro.fateev.diary.feature.mainscreen.MainScreenViewModel
import pro.fateev.diary.feature.pin.ui.PINScreen
import pro.fateev.diary.feature.pin.ui.PINScreenViewModel
import pro.fateev.diary.feature.pin.ui.SetPINQuestionViewModel
import pro.fateev.diary.feature.settings.ui.SettingsScreen
import pro.fateev.diary.feature.settings.ui.SettingsViewModel
import pro.fateev.diary.navigation.NavigationComponent
import pro.fateev.diary.navigation.NavigationController
import pro.fateev.diary.navigation.NavigationControllerImpl
import pro.fateev.diary.navigation.Routes
import pro.fateev.diary.navigation.composable
import pro.fateev.diary.ui.theme.AppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var controller: NavigationController

    private val vm: MainScreenViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostState = rememberNavController()
            controller = NavigationControllerImpl(navHostState)
            vm.setNavigationController(controller)
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

                        composable<PINScreenViewModel>(
                            route = Routes.PIN(),
                            navigationController = controller
                        ) { _, vm ->
                            PINScreen(vm)
                        }

                        composable<SetPINQuestionViewModel>(
                            route = Routes.SetPINQuestion,
                            navigationController = controller
                        ) { _, vm ->
                            YesNoScreen(
                                title = "Set PIN?",
                                description = "To secure the diary you may set a PIN",
                                onPositive = vm::onAgree,
                                onNegative = vm::onDisagree
                            )
                        }

                        composable<SettingsViewModel>(
                            route = Routes.Settings,
                            navigationController = controller
                        ) { _, vm ->
                            SettingsScreen(vm)
                        }
                    }
                }
            }

            vm.onReturnToApp()
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO: maybe move to lifecycle inside of VM?
        vm.onReturnToApp()
    }

    override fun onPause() {
        super.onPause()
        // TODO: maybe move to lifecycle inside of VM?
        vm.onLeaveApp()
    }
}