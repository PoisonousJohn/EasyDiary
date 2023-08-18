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

package pro.fateev.diary.feature.diary.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import pro.fateev.diary.feature.diary.data.DiaryEntryMapper.toDomainModel
import pro.fateev.diary.feature.diary.data.DiaryEntryMapper.toEntity
import pro.fateev.diary.feature.diary.data.room.AppDatabase
import pro.fateev.diary.feature.diary.domain.DiaryRepository
import pro.fateev.diary.feature.diary.domain.MediaRepository
import pro.fateev.diary.feature.diary.domain.model.Diary
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val scope: CoroutineScope,
    appDatabase: AppDatabase,
    private val _mediaRepo: MediaRepository,
) : DiaryRepository {

    private val _diaryFlow = MutableSharedFlow<Diary>(replay = 1)
    private val _diaryDAO = appDatabase.diaryEntryDAO

    init {
        notifyUpdated()
    }

    override fun getDiary(): Flow<Diary> = _diaryFlow

    override fun getDiaryEntry(id: Long): Flow<DiaryEntry> =
        flow {
            val diaryEntry = _diaryDAO.getById(id)
                .toDomainModel()
            val media = _mediaRepo.getMediaByDiaryEntryId(id)
            emit(diaryEntry.copy(media = media))
        }

    override suspend fun saveDiaryEntry(entry: DiaryEntry): DiaryEntry {
        var entryId = entry.id
        if (entry.id < 0) {
            entryId = _diaryDAO.insert(entry.toEntity()).first()
        } else {
            _diaryDAO.update(entry.toEntity())
        }
        val media = entry.media.toMutableList()
        media.mapIndexed { idx, item ->
            if (item.id < 0) {
                val newMedia = _mediaRepo.addMedia(entryId, item)
                media[idx] = newMedia
            }
        }
        notifyUpdated()
        return entry.copy(id = entryId, media = media)
    }

    private fun notifyUpdated() = scope.launch {
        scope.launch {
            _diaryDAO.getAll()
                .map {
                    val media = _mediaRepo.getMediaByDiaryEntryId(it.id!!)
                    it.toDomainModel().copy(media = media)
                }
                .let(::Diary)
                .let(_diaryFlow::tryEmit)
        }
    }

}