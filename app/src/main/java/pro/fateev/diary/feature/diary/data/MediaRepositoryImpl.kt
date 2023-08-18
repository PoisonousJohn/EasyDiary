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

import pro.fateev.diary.ImageUtils
import pro.fateev.diary.feature.diary.data.MediaMapper.toDomainModel
import pro.fateev.diary.feature.diary.data.MediaMapper.toEntity
import pro.fateev.diary.feature.diary.data.room.AppDatabase
import pro.fateev.diary.feature.diary.data.room.MediaChunkEntity
import pro.fateev.diary.feature.diary.domain.MediaRepository
import pro.fateev.diary.feature.diary.domain.model.Media
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    _appDatabase: AppDatabase
) : MediaRepository {

    private val _mediaDAO = _appDatabase.mediaDAO

    override suspend fun addMedia(diaryEntryId: Long, media: Media): Media {
        val mediaId = _mediaDAO.insertMediaAndChunks(
            media.toEntity(diaryEntryId),
            ImageUtils.sliceInChunks(media.data, 1024 * 1024)
                .map { MediaChunkEntity(id = null, mediaId = -1, data = it) })
        return media.copy(id = mediaId)
    }

    override suspend fun getMediaByDiaryEntryId(diaryEntryId: Long): List<Media> {
        val media = _mediaDAO.getByDiaryEntryId(diaryEntryId)
        return media.map {
            val chunks = _mediaDAO.getDataChunkByMediaId(it.id ?: error("missing id"))
            it.toDomainModel(chunks)
        }
    }

    override suspend fun removeMedia(diaryEntryId: Long, index: Int) {
        val media = _mediaDAO.getByDiaryEntryId(diaryEntryId)[index]
        _mediaDAO.delete(media)
    }

    override suspend fun getMediaByMediaId(mediaId: Long): Media {
        val media = _mediaDAO.getById(mediaId)
        val chunks = _mediaDAO.getDataChunkByMediaId(mediaId)
        return media.toDomainModel(chunks)
    }
}