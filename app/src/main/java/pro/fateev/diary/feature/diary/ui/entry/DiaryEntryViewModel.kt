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

package pro.fateev.diary.feature.diary.ui.entry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import pro.fateev.diary.extensions.FlowExtensions.mutableStateIn
import pro.fateev.diary.feature.diary.domain.DiaryRepository
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import pro.fateev.diary.ui.screen.common.BaseViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val repo: DiaryRepository,
    savedState: SavedStateHandle
) : BaseViewModel() {

    private val _entryId = savedState.get<Long>("id")
    private val _diaryEntry: MutableStateFlow<DiaryEntry> = repo.getDiary().map {
        it.entries.firstOrNull { entry ->
            entry.id == (_entryId ?: -1L)
        } ?: DiaryEntry()
    }.mutableStateIn(viewModelScope, DiaryEntry())

    val data: StateFlow<DiaryEntry>
        get() = _diaryEntry

    fun onChangeDate(date: Date) {
        viewModelScope.launch {
            _diaryEntry.emit(_diaryEntry.value.copy(date = date))
        }
    }

    fun onTextChanged(text: String) {
        _diaryEntry.value = _diaryEntry.value.copy(text = text)
    }

    fun onSave() {
        viewModelScope.launch {
            if (_diaryEntry.value.id == -1L) {
                repo.saveDiaryEntry(_diaryEntry.value)
            }
            pop()
        }
    }
}