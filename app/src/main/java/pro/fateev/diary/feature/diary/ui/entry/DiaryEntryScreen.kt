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

package pro.fateev.diary.feature.diary.ui.entry

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pro.fateev.diary.ui.theme.AppTheme

@Composable
fun DiaryEntryScreen(vm: DiaryEntryViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title =  { Text("asdf") },
                actions = {
                    TextButton(onClick = vm::onSave) {
                        Text("Save")
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxHeight()
            .fillMaxWidth()) {
            val text = vm.text.collectAsState().value
            TextField(value = text, onValueChange = { it: String ->
                vm.onTextChanged(it)
            }, textStyle = MaterialTheme.typography.body1, modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth())
        }
    }
}

@Composable
@Preview
fun DiaryEntryScreenPreview() {
    AppTheme(darkTheme = true) {
        DiaryEntryScreen(vm = DiaryEntryViewModel())
    }
}