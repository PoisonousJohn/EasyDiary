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

package pro.fateev.diary.feature.diary

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import pro.fateev.diary.feature.diary.domain.DiaryRepository
import pro.fateev.diary.feature.diary.domain.model.Diary
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(private val scope: CoroutineScope) : DiaryRepository {
    private var diary = Diary(
        entries = listOf(
            DiaryEntry(1, "Test 1"),
            DiaryEntry(2, "Test 2"),
            DiaryEntry(3, "Test 3"),
        )
    )

    private val entries = MutableSharedFlow<Diary>(1)

    init {
        scope.launch {
            entries.emit(diary)
        }
    }

    override fun getDiary(): Flow<Diary> = entries
    override suspend fun addDiaryEntry(entry: DiaryEntry) {
        diary = diary.copy(entries = diary.entries.toMutableList().apply {
            add(entry.copy(id = (diary.entries.lastOrNull()?.id ?: 0) + 1))
        })
        notifyUpdated()
    }

    private fun notifyUpdated() = scope.launch {
        entries.emit(diary)
    }
}