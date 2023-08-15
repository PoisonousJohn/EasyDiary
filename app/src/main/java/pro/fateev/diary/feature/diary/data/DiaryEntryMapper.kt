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

import pro.fateev.diary.feature.diary.data.MediaMapper.toDomainModel
import pro.fateev.diary.feature.diary.data.room.DiaryEntryEntity
import pro.fateev.diary.feature.diary.data.room.DiaryEntryMedia
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry

object DiaryEntryMapper {
    fun DiaryEntry.toEntity(): DiaryEntryEntity =
        DiaryEntryEntity(id = if (id == -1L) null else id, text = text, date = date)

    fun DiaryEntryEntity.toDomainModel(): DiaryEntry =
        DiaryEntry(id = id ?: -1, text = text ?: "", date = date)

    fun DiaryEntryMedia.toDomainModel(): DiaryEntry =
        DiaryEntry(
            id = diaryEntry.id ?: -1,
            text = diaryEntry.text ?: "",
            date = diaryEntry.date,
            media = media.map { it.toDomainModel() })
}