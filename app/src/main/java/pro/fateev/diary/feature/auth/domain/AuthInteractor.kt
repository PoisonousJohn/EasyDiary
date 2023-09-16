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

package pro.fateev.diary.feature.auth.domain

import pro.fateev.diary.feature.pin.domain.PINRepository
import javax.inject.Inject
import javax.inject.Singleton

enum class AuthCheckResult {
    PINNotSet,
    NeedAuthorization,
    Ok
}

@Singleton
class AuthInteractor @Inject constructor(
    private val authRepo: AuthRepository,
    private val pinRepo: PINRepository
) {
    fun isPINValid(pin: String) = pinRepo.isPINValid(pin)
    fun getPINLength() = pinRepo.getPINLength()
    fun onReturnToApp(): AuthCheckResult {
        if (pinRepo.isPINSet().not()) return AuthCheckResult.PINNotSet

        authRepo.tryExtendAuth()
        return when {
            authRepo.isAuthExpired() -> return AuthCheckResult.NeedAuthorization
            else -> AuthCheckResult.Ok
        }
    }

    fun isSetPINQuestionNeeded() = pinRepo.isPINSet().not() && pinRepo.isPINQuestionAsked().not()
    fun onSetPINQuestionAsked() = pinRepo.onPINQuestionAsked()
    fun setPIN(pin: String) {
        pinRepo.setPIN(pin)
        authRepo.approveAuth()
    }
}