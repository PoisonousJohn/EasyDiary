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

@Dao
interface MediaDAO {
    @Query("SELECT * FROM media WHERE id = :id")
    suspend fun getById(id: Long): MediaEntity

    @Query("SELECT * FROM media WHERE diary_entry_id = :diaryEntryId")
    suspend fun getByDiaryEntryId(diaryEntryId: Long): Array<MediaEntity>

    @Insert
    suspend fun insert(media: MediaEntity): Long

    @Delete
    suspend fun delete(media: MediaEntity): Int

    @Insert
    suspend fun insert(mediaChunk: List<MediaChunkEntity>): Array<Long>

    @Transaction
    suspend fun insertMediaAndChunks(media: MediaEntity, chunks: List<MediaChunkEntity>): Long {
        val mediaId = insert(media)
        insert(chunks.map { it.copy(mediaId = mediaId) })

        return mediaId
    }

    @Query("SELECT data FROM media_chunk WHERE media_id = :mediaId")
    suspend fun getDataChunkByMediaId(mediaId: Long): Array<ByteArray>
}