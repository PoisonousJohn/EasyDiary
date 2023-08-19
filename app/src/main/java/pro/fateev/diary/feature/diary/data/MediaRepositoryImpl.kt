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

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.fateev.diary.feature.diary.data.MediaMapper.toDomainModel
import pro.fateev.diary.feature.diary.data.MediaMapper.toEntity
import pro.fateev.diary.feature.diary.data.MediaMapper.toNotAttachedMediaEntity
import pro.fateev.diary.feature.diary.data.room.AppDatabase
import pro.fateev.diary.feature.diary.domain.MediaRepository
import pro.fateev.diary.feature.diary.domain.model.Media
import java.io.File
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    @ApplicationContext appContext: Context,
    _appDatabase: AppDatabase
) : MediaRepository {
    private val _filesDir = appContext.filesDir
    private val _mediaDAO = _appDatabase.mediaDAO

    private val ioScope = CoroutineScope(Dispatchers.IO)


    override suspend fun addMedia(diaryEntryId: Long, media: Media): Media {
        val mediaId =
            _mediaDAO.insert(media.copy(pathToFile = generateFileName()).toEntity(diaryEntryId))
        return media.copy(id = mediaId)
    }

    override suspend fun getMediaByDiaryEntryId(diaryEntryId: Long): List<Media> {
        val media = _mediaDAO.getByDiaryEntryId(diaryEntryId)
        return media.map { it.toDomainModel() }
    }

    override suspend fun removeMedia(diaryEntryId: Long, index: Int) {
        val media = _mediaDAO.getByDiaryEntryId(diaryEntryId)[index]
        _mediaDAO.delete(media)
    }

    override suspend fun getMediaByMediaId(mediaId: Long): Media =
        _mediaDAO.getById(mediaId).toDomainModel()

    override suspend fun saveDraftFile(data: ByteArray): Media {
        val filename = generateFileName()
        ioScope.launch {
            File(filename).writeBytes(data)
        }
        val media = Media(id = -1, pathToFile = filename)
        val id = _mediaDAO
            .insert(media.toNotAttachedMediaEntity())
        return media.copy(id = id)
    }

    override suspend fun attachToDiaryEntry(media: List<Media>, diaryEntryId: Long) {
        val entities = media.map { it.toEntity(diaryEntryId) }
        _mediaDAO.update(entities)
    }

    override suspend fun removeDraftMedia() {
        val draftMedia = _mediaDAO.getDraftMedia()
        ioScope.launch {
            for (media in draftMedia) {
                File(media.pathToFile).delete()
            }
        }
        _mediaDAO.delete(draftMedia)
    }

    private fun generateFileName(): String =
        "${_filesDir}/${UUID.randomUUID()}_${Calendar.getInstance().time.time}.media"
}