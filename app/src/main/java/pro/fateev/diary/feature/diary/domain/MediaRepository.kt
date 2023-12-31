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

package pro.fateev.diary.feature.diary.domain

import pro.fateev.diary.feature.diary.domain.model.Media

interface MediaRepository {
    suspend fun addMedia(diaryEntryId: Long, media: Media): Media
    suspend fun removeMedia(diaryEntryId: Long, index: Int)
    suspend fun getMediaByDiaryEntryId(diaryEntryId: Long): List<Media>
    suspend fun getMediaByMediaId(mediaId: Long): Media
    suspend fun attachToDiaryEntry(media: List<Media>, diaryEntryId: Long)

    suspend fun removeDraftMedia()

    /**
     * @return media that is not attached to any entry
     */
    suspend fun saveDraftFile(data: ByteArray): Media
}