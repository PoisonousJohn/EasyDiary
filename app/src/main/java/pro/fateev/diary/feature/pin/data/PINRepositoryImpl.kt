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

package pro.fateev.diary.feature.pin.data

import android.content.SharedPreferences
import pro.fateev.diary.feature.pin.domain.PINRepository
import javax.inject.Inject

class PINRepositoryImpl @Inject constructor(private val sharedPrefs: SharedPreferences) :
    PINRepository {

    override fun isPINSet(): Boolean = sharedPrefs.contains(PINKey)

    override fun isPINQuestionAsked(): Boolean = sharedPrefs.getBoolean(PINQuestionKey, false)

    override fun setPIN(pin: String) = sharedPrefs.edit().apply {
        putString(PINKey, pin)
        onPINQuestionAsked()
    }.apply()

    override fun onPINQuestionAsked() {
        sharedPrefs.edit().putBoolean(PINQuestionKey, true).apply()
    }

    override fun isPINValid(pin: String): Boolean =
        isPINSet() && pin == sharedPrefs.getString(PINKey, null)

    override fun getPINLength(): Int = PINLength

    private companion object {
        const val PINQuestionKey = "pin.question"
        const val PINKey = "pin"
        const val PINLength = 5
    }
}