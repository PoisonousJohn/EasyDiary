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

package pro.fateev.diary.feature.settings.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.feature.diary.ui.components.MyTopAppBar
import pro.fateev.diary.ui.theme.AppTheme

@Composable
fun SettingsScreen(vm: SettingsViewModel) {
    val isPINSet = vm.isPINSet.collectAsState(initial = false).value
    SettingsScreenContent(
        isPINSet = isPINSet,
        onBack = vm::pop,
        onSetPIN = vm::setPIN,
        onRemovePIN = vm::removePIN
    )
}

@Composable
fun SettingsEntry(icon: ImageVector, text: String, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick::invoke)
            .padding(12.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, text, modifier = Modifier.size(32.dp))
        Text(
            text,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Composable
fun SettingsScreenContent(
    isPINSet: Boolean = true,
    onBack: () -> Unit = {},
    onSetPIN: () -> Unit = {},
    onRemovePIN: () -> Unit = {}
) {
    Scaffold(
        topBar = { MyTopAppBar(title = "Settings", onBack = onBack) },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            ) {
                SettingsEntry(
                    icon = Icons.Default.Key,
                    text = if (isPINSet) "Change PIN" else "Setup PIN",
                    onClick = onSetPIN
                )

                if (isPINSet) {
                    SettingsEntry(
                        icon = Icons.Default.LockReset,
                        text = "Remove PIN",
                        onClick = onRemovePIN
                    )

                }
            }
        })
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun SettingsScreenPreview() {
    AppTheme {
        SettingsScreenContent()
    }
}