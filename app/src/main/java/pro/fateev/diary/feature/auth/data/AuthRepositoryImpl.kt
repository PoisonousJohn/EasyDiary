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

package pro.fateev.diary.feature.auth.data

import android.content.SharedPreferences
import pro.fateev.diary.extensions.DateExtensions.toInstant
import pro.fateev.diary.feature.auth.domain.AuthRepository
import java.time.Duration
import java.time.Instant
import java.util.Calendar
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val sharedPreferences: SharedPreferences) :
    AuthRepository {
    override fun isAuthExpired(): Boolean {
        if (!sharedPreferences.contains(LastAuthTimestamp)) return true

        val lastAuth = sharedPreferences.getLong(LastAuthTimestamp, 0).toInstant()
        val now = Instant.now()
        val duration = Duration.between(lastAuth, now)
        return duration.seconds <= AuthTimeoutSeconds
    }

    override fun tryExtendAuth() {
        if (isAuthExpired()) return
        approveAuth()
    }

    override fun approveAuth() =
        sharedPreferences.edit().apply {
            val date = Calendar.getInstance().time.time
            putLong(LastAuthTimestamp, date)
        }.apply()

    private companion object {
        const val LastAuthTimestamp = "last.auth.timestamp"
        const val AuthTimeoutSeconds = 15
    }
}