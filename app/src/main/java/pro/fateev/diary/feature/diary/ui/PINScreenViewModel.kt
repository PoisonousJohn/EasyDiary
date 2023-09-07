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

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import pro.fateev.diary.feature.pin.domain.PINRepository
import pro.fateev.diary.ui.screen.common.BaseViewModel
import javax.inject.Inject

class PINScreenViewModel @Inject constructor(
    private val _pinRepo: PINRepository
) : BaseViewModel() {

    private var _pin = ""
    private var _mutablePINFlow = MutableStateFlow<PINStatus>(PINStatus.Filled(0))

    val pinStatus: Flow<PINStatus> = _mutablePINFlow
    val pinLength = _pinRepo.getPINLength()

    fun onBackspace() {
        _pin = _pin.substring(0, _pin.length)
        updateFilledState()
    }

    fun onEnterNumber(number: Int) {
        _pin += number
        if (_pin.length == _pinRepo.getPINLength()) {
            val isPinValid = _pinRepo.isPINValid(_pin)
            _pin = ""
            viewModelScope.launch {
                if (isPinValid) {
                    _mutablePINFlow.emit(PINStatus.Success)
                } else {
                    _mutablePINFlow.emit(PINStatus.Error)
                }
            }
            return
        }

    }

    fun onPINConfirmed() {
        pop()
    }

    private fun updateFilledState() {
        viewModelScope.launch {
            _mutablePINFlow.emit(PINStatus.Filled(_pin.length))
        }
    }
}