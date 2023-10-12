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

package pro.fateev.diary.feature.mainscreen

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pro.fateev.diary.feature.auth.domain.AuthCheckResult
import pro.fateev.diary.feature.auth.domain.AuthInteractor
import pro.fateev.diary.feature.pin.ui.PINScreenViewModel
import pro.fateev.diary.navigation.Routes
import pro.fateev.diary.navigation.routing.generatePath
import pro.fateev.diary.ui.screen.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {
    fun onReturnToApp() {
        if (authInteractor.onReturnToApp() == AuthCheckResult.NeedAuthorization) {
            viewModelScope.launch {
                navigateTo(Routes.PIN().generatePath(Routes.PIN.modeKey to PINScreenViewModel.Mode.Auth))
            }
            return
        }

        if (authInteractor.isSetPINQuestionNeeded()) {
            viewModelScope.launch {
                navigateTo(Routes.SetPINQuestion.generatePath())
            }
        }
    }

    fun onLeaveApp() {
        authInteractor.onLeaveApp()
    }
}