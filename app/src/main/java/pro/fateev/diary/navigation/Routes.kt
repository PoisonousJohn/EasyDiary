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

package pro.fateev.diary.navigation

import androidx.navigation.NavType
import pro.fateev.diary.feature.pin.ui.PINScreenViewModel
import pro.fateev.diary.navigation.routing.ScreenRoute

object Routes {

    object Diary : ScreenRoute(
        routeDefinition = Definition("diary")
    )

    object DiaryEntry : ScreenRoute(
        routeDefinition = Definition("diary-entry", argumentKeys = listOf(
            "id" to { type = NavType.LongType; optional = false }
        ))
    )

    object ImagePreview : ScreenRoute(
        routeDefinition = Definition("image-preview", argumentKeys = listOf(
            "id" to { type = NavType.LongType; optional = false }
        ))
    )

    class PIN : ScreenRoute(
        routeDefinition = Definition(
            "pin", argumentKeys = listOf(
                modeKey to { type = NavType.EnumType(PINScreenViewModel.Mode::class.java) }
            )
        )
    ) {
        companion object {
            const val modeKey = "mode"
        }
    }

    object SetPINQuestion : ScreenRoute(routeDefinition = Definition("set-pin-question"))

    object Settings : ScreenRoute(routeDefinition = Definition("settings"))
}