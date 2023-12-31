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

package pro.fateev.diary.feature.diary.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface MediaDAO {
    @Query("SELECT * FROM media WHERE id = :id")
    suspend fun getById(id: Long): MediaEntity

    @Query("SELECT * FROM media WHERE diary_entry_id = :diaryEntryId")
    suspend fun getByDiaryEntryId(diaryEntryId: Long): Array<MediaEntity>

    @Query("SELECT * FROM media WHERE diary_entry_id is null")
    suspend fun getDraftMedia(): Array<MediaEntity>

    @Insert
    suspend fun insert(media: MediaEntity): Long

    @Delete
    suspend fun delete(media: MediaEntity): Int

    @Delete
    suspend fun delete(media: Array<MediaEntity>): Int

    @Transaction
    @Update
    suspend fun update(media: List<MediaEntity>)

}