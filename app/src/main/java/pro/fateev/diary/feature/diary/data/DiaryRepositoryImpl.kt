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
import pro.fateev.diary.feature.diary.data.MediaMapper.toDomainModel
import pro.fateev.diary.feature.diary.data.MediaMapper.toEntity
import pro.fateev.diary.feature.diary.data.room.AppDatabase
import pro.fateev.diary.feature.diary.domain.DiaryRepository
import pro.fateev.diary.feature.diary.domain.model.Diary
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val scope: CoroutineScope,
    appDatabase: AppDatabase
) : DiaryRepository {

    private val _diaryFlow = MutableSharedFlow<Diary>(replay = 1)
    private val _diaryDAO = appDatabase.diaryEntryDAO
    private val _mediaDAO = appDatabase.mediaDAO

    init {
        notifyUpdated()
    }

    override fun getDiary(): Flow<Diary> = _diaryFlow

    override fun getDiaryEntry(id: Long): Flow<DiaryEntry> =
        flow {
            val media = _mediaDAO.getByDiaryEntryId(id)
            _diaryDAO.getById(id)
                .toDomainModel()
                .copy(media =  media.map { it.toDomainModel() })
                .let { emit(it) }
        }

    override suspend fun saveDiaryEntry(entry: DiaryEntry) {
        entry.media.map { media ->
            if (media.id < 0) _mediaDAO.insert(media.toEntity(entry.id))
        }
        if (entry.id < 0) {
            _diaryDAO.insert(entry.toEntity()).first()
        } else {
            _diaryDAO.update(entry.toEntity())
        }
        notifyUpdated()
    }

    override suspend fun removeMedia(diaryEntryId: Long, index: Int) {
        val media = _mediaDAO.getByDiaryEntryId(diaryEntryId)[index]
        _mediaDAO.delete(media)
    }

    private fun notifyUpdated() = scope.launch {
        scope.launch {
            val result = _diaryDAO.getAll()
                .map { it.toDomainModel() }
                .let(::Diary)
                .let(_diaryFlow::tryEmit)
        }
    }
}