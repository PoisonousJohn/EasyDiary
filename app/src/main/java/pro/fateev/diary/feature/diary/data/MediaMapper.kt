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

import pro.fateev.diary.feature.diary.data.room.MediaEntity
import pro.fateev.diary.feature.diary.domain.model.Media

object MediaMapper {
    fun Media.toEntity(diaryEntryId: Long): MediaEntity =
        MediaEntity(
            id = if (id == -1L) null else id,
            data = data,
            diaryEntryId = diaryEntryId,
            mimeType = "image/*"
        )

    fun MediaEntity.toDomainModel(): Media =
        Media(id = id ?: -1, data = data)
}