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

package pro.fateev.diary.ui.screen.diary.entry

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import pro.fateev.diary.navigation.routing.generatePath
import pro.fateev.diary.ui.screen.Routes
import pro.fateev.diary.ui.screen.common.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor() : BaseViewModel() {
    val entryId = MutableStateFlow<Int?>(null)
    private val _text = MutableStateFlow<String>("")
    val text: StateFlow<String> = _text

    fun onTextChanged(text: String) {
        _text.value = text
    }
}