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
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DiaryEntryDAO {

    @Query("SELECT * FROM diary_entry")
    suspend fun getAll(): Array<DiaryEntryEntity>

    @Insert
    suspend fun insert(vararg entry: DiaryEntryEntity): List<Long>

    @Query("DELETE FROM diary_entry WHERE id = :id")
    suspend fun delete(id: Int): Int

}