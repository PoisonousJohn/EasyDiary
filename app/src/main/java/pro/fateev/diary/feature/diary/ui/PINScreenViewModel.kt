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

package pro.fateev.diary.feature.diary.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pro.fateev.diary.feature.auth.domain.AuthInteractor
import pro.fateev.diary.ui.screen.Routes
import pro.fateev.diary.ui.screen.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class PINScreenViewModel @Inject constructor(
    private val _authInteractor: AuthInteractor,
    savedState: SavedStateHandle,
) : BaseViewModel() {

    private var _savedPIN = ""
    private var _pin = ""
    private var _mutablePINFlow = MutableStateFlow<PINStatus>(PINStatus.Filled(0))
    private var _modeFlow = MutableStateFlow(
        savedState.get<Mode>(Routes.PIN.modeKey) ?: error("mode is missing")
    )

    val pinStatus: Flow<PINStatus> = _mutablePINFlow
    val pinLength = _authInteractor.getPINLength()
    val mode: Flow<Mode> = _modeFlow

    fun onBackspace() {
        if (_pin.isBlank()) return
        _pin = _pin.substring(0, _pin.length - 1)
        updateFilledState()
    }

    fun onEnterNumber(number: Int) {
        _pin += number
        updateFilledState()

        if (_modeFlow.value == Mode.Auth && _pin.length == _authInteractor.getPINLength()) {
            checkAuth()
            return
        }

        checkPINSet()
    }

    private fun checkPINSet() {
        if (_pin.length < _authInteractor.getPINLength()) return

        if (_modeFlow.value == Mode.SetPIN) {
            _savedPIN = _pin
            _pin = ""
            viewModelScope.launch {
                _modeFlow.emit(Mode.RepeatPIN)
            }
            updateFilledState()
            return
        }

        if (_savedPIN != _pin) {
            onPINDoesNotMatchError()
            return
        }

        _authInteractor.setPIN(_savedPIN)
        pop()
    }

    private fun onPINDoesNotMatchError() {
        _savedPIN = ""
        _pin = ""
        viewModelScope.launch {
            _modeFlow.emit(Mode.SetPIN)
            _mutablePINFlow.emit(PINStatus.Error(PINError.PINDoesNotMatch))
        }
    }

    private fun checkAuth() {
        val isPinValid = _authInteractor.isPINValid(_pin)
        _pin = ""
        viewModelScope.launch {
            if (isPinValid) {
                _mutablePINFlow.emit(PINStatus.Success)
                pop()
            } else {
                _mutablePINFlow.emit(PINStatus.Error(PINError.WrongPIN))
            }
        }
        return
    }

    private fun updateFilledState() {
        viewModelScope.launch {
            _mutablePINFlow.emit(PINStatus.Filled(_pin.length))
        }
    }

    enum class Mode {
        Auth,
        SetPIN,
        RepeatPIN
    }
}