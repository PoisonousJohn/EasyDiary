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

package pro.fateev.diary.feature.diary.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pro.fateev.diary.ui.theme.appBar

@Composable
fun MyTopAppBar(
    title: String? = null,
    actions: @Composable RowScope.() -> Unit = {},
    onBack: (() -> Unit)? = null
) =
    TopAppBar(
        backgroundColor = MaterialTheme.colors.appBar,
        navigationIcon = onBack?.let {
            {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        "Back",
                        modifier = Modifier.clickable(onClick = onBack)
                    )
                }
            }
        },
        title = {
            if (title != null) {
                Text(text = title, style = MaterialTheme.typography.h5)
            }
        },
        actions = actions
    )
