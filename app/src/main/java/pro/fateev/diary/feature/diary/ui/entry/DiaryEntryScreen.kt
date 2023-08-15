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

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.extensions.DateExtensions.showDatePicker
import pro.fateev.diary.extensions.FormattingExtensions.formatShort
import pro.fateev.diary.feature.diary.domain.model.Media
import pro.fateev.diary.ui.theme.AppTheme
import java.util.Date

@Composable
fun DiaryEntryScreen(vm: DiaryEntryViewModel) {
    val state = vm.data.collectAsState().value
    DiaryEntryScreenContent(
        text = state.text,
        onTextChanged = vm::onTextChanged,
        onSave = vm::onSave,
        date = state.date,
        media = state.media,
        onChangeDate = vm::onChangeDate,
        onAttachFile = vm::onAttachFile
    )
}

@Composable
fun DiaryEntryScreenContent(
    date: Date,
    text: String,
    media: List<Media> = emptyList(),
    onTextChanged: (String) -> Unit,
    onSave: () -> Unit,
    onChangeDate: (Date) -> Unit,
    onAttachFile: (Uri?) -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = { AppBar(date, context, onChangeDate, onSave) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val pickPictureLauncher = rememberLauncherForActivityResult(
                ActivityResultContracts.GetContent(),
                onResult = { uri -> onAttachFile(uri) }
            )
            TextField(
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                value = text,
                onValueChange = onTextChanged,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            if (media.isNotEmpty()) {
                Row {
                    for (m in media) {
                        val imageBitmap = m.data.toBitmap().asImageBitmap()
                        Image(
                            bitmap = imageBitmap,
                            contentDescription = "",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                }
            }
            Button(onClick = { pickPictureLauncher.launch("image/*") }) {
                Text("Attach file")
            }
        }
    }
}

fun ByteArray.toBitmap(): Bitmap {
    return BitmapFactory.decodeByteArray(this, 0, this.size)
}

@Composable
private fun AppBar(
    date: Date,
    context: Context,
    onChangeDate: (Date) -> Unit,
    onSave: () -> Unit
) = TopAppBar {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable(onClick = {
                date.showDatePicker(context, onChangeDate)
            })
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                modifier = Modifier.padding(end = 4.dp),
                imageVector = Icons.Outlined.CalendarMonth,
                contentDescription = "icon"
            )
            Text(text = date.formatShort(LocalContext.current))
            Icon(
                imageVector = Icons.Outlined.ArrowDropDown,
                contentDescription = "icon"
            )
        }
        Row(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {

            TextButton(onClick = onSave) {
                Text("Save", color = Color.White)
            }
        }
    }
}

@Composable
@Preview
fun DiaryEntryScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        DiaryEntryScreenContent(
            text = "test",
            date = Date(),
            onSave = {},
            onTextChanged = {},
            onChangeDate = {},
            onAttachFile = {})
    }
}

@Composable
@Preview
fun DiaryEntryScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        DiaryEntryScreenContent(
            text = "test",
            date = Date(),
            onSave = {},
            onTextChanged = {},
            onChangeDate = {},
            onAttachFile = {})
    }
}