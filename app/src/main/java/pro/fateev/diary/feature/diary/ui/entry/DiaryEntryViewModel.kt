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

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.fateev.diary.ImageUtils.toEXIFAwareImageBytes
import pro.fateev.diary.extensions.FlowExtensions.mutableStateIn
import pro.fateev.diary.feature.diary.domain.DiaryRepository
import pro.fateev.diary.feature.diary.domain.MediaRepository
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import pro.fateev.diary.feature.diary.domain.model.Media
import pro.fateev.diary.navigation.Routes
import pro.fateev.diary.navigation.routing.generatePath
import pro.fateev.diary.ui.screen.common.BaseViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class DiaryEntryViewModel @Inject constructor(
    private val _diaryRepo: DiaryRepository,
    private val _mediaRepo: MediaRepository,
    savedState: SavedStateHandle,
    @ApplicationContext context: Context
) : BaseViewModel() {

    private val _entryId = savedState.get<Long>("id")
    private val _contentResolver = context.contentResolver
    private val _mediaBuffer = mutableListOf<Media>()

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val _diaryEntry: MutableStateFlow<DiaryEntry> =
        (if (_entryId == -1L || _entryId == null) flowOf(DiaryEntry())
        else _diaryRepo.getDiaryEntry(_entryId))
            .onEach {
                _mediaBuffer.clear()
                _mediaBuffer.addAll(it.media)
            }
            .mutableStateIn(viewModelScope, DiaryEntry())


    val data: StateFlow<DiaryEntry>
        get() = _diaryEntry

    fun onChangeDate(date: Date) {
        updateEntry { it.copy(date = date) }
    }

    fun onTextChanged(text: String) {
        _diaryEntry.value = _diaryEntry.value.copy(text = text)
    }

    fun onSave() {
        viewModelScope.launch {
            val id = _diaryRepo.saveDiaryEntry(_diaryEntry.value).id
            _mediaRepo.attachToDiaryEntry(_mediaBuffer, id)
            pop()
        }
    }

    fun onAttachMedia(uri: Uri?) {
        if (uri == null) return

        viewModelScope.launch {
            var bytes: ByteArray
            withContext(ioScope.coroutineContext) {
                 bytes = uri.toEXIFAwareImageBytes(_contentResolver)
            }
            val draftMedia = _mediaRepo.saveDraftFile(bytes)
            _mediaBuffer.add(draftMedia)
            updateMedia()
        }
    }

    fun onOpenImagePreview(index: Int) {
        viewModelScope.launch {
            val path = Routes.ImagePreview.generatePath("id" to _mediaBuffer[index].id)
            navigateTo(path)
        }
    }

    fun onDeleteMedia(index: Int) {
        _mediaBuffer.removeAt(index)
        viewModelScope.launch {
            _mediaRepo.removeMedia(_diaryEntry.value.id, index)
            updateMedia()
        }
    }

    fun onBack() {
        _mediaBuffer.clear()
        updateMedia()
        ioScope.launch {
            _mediaRepo.removeDraftMedia()
        }
        pop()
    }

    private fun updateMedia() {
        updateEntry { it.copy(media = _mediaBuffer.toList()) }
    }

    private fun updateEntry(updateBlock: (DiaryEntry) -> DiaryEntry) {
        viewModelScope.launch {
            _diaryEntry.emit(updateBlock.invoke(_diaryEntry.value))
        }
    }
}