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
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.R
import pro.fateev.diary.ImageUtils.toBitmap
import pro.fateev.diary.extensions.DateExtensions.showDatePicker
import pro.fateev.diary.extensions.FormattingExtensions.formatShort
import pro.fateev.diary.ui.theme.AppTheme
import pro.fateev.diary.ui.theme.text
import java.util.Date

@Composable
fun DiaryEntryScreen(vm: DiaryEntryViewModel) {
    val state = vm.data.collectAsState().value
    DiaryEntryScreenContent(
        text = state.text,
        onTextChanged = vm::onTextChanged,
        onSave = vm::onSave,
        date = state.date,
        images = state.media.map { m ->
            m.data.toBitmap().asImageBitmap().let { BitmapPainter(it) }
        },
        onChangeDate = vm::onChangeDate,
        onAttachFile = vm::onAttachMedia,
        onImageClick = vm::onOpenImagePreview,
        onDeleteMedia = vm::onDeleteMedia,
    )
}

@Composable
fun DiaryEntryScreenContent(
    date: Date = Date(),
    text: String = "",
    images: List<Painter> = emptyList(),
    onTextChanged: (String) -> Unit = {},
    onSave: () -> Unit = {},
    onChangeDate: (Date) -> Unit = {},
    onAttachFile: (Uri?) -> Unit = {},
    onImageClick: (index: Int) -> Unit = {},
    onDeleteMedia: (index: Int) -> Unit = {}
) {
    val context = LocalContext.current
    Scaffold(
        topBar = { AppBar(date, context, onChangeDate, onSave) },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxHeight()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (images.isNotEmpty()) {
                AttachedMedia(
                    images = images,
                    onDelete = onDeleteMedia,
                    onImageClick = onImageClick
                )
            }
            BasicTextField(
                value = text,
                onValueChange = onTextChanged,
                textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.text),
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.surface)
                            .padding(horizontal = 12.dp)
                    ) { innerTextField() }
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            ToolsPanel(onAttachFile)
        }
    }
}

@Composable
private fun ToolsPanel(onAttachFile: (Uri?) -> Unit) {
    val pickPictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onAttachFile(uri) }
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
    ) {
        Row {
            IconButton(onClick = {
                pickPictureLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }, modifier = Modifier.size(44.dp)) {
                Icon(Icons.Filled.Image, contentDescription = "")
            }
        }
    }
}

@Composable
private fun AttachedMedia(
    images: List<Painter>,
    onImageClick: (index: Int) -> Unit,
    onDelete: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val roundingDp = 12.dp
    val shape = RoundedCornerShape(roundingDp)
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(roundingDp, alignment = Alignment.Start),
    ) {
        items(images.size) {
            val img = images[it]
            Box(modifier = Modifier.padding(roundingDp), contentAlignment = Alignment.TopEnd)
            {
                Image(
                    contentScale = ContentScale.Crop,
                    painter = img,
                    contentDescription = "",
                    modifier = Modifier
                        .clickable { onImageClick.invoke(it) }
                        .size(100.dp)
                        .clip(shape)
                        .shadow(8.dp, shape)
                )
                IconButton(
                    onClick = { onDelete(it) }, modifier = Modifier
                        .offset(8.dp, (-8).dp)
                        .wrapContentSize(unbounded = true)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colors.onError
                    )
                }
            }
        }
    }
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
            images = listOf(
                painterResource(id = R.drawable.ic_launcher_background),
                painterResource(id = R.drawable.ic_launcher_background),
                painterResource(id = R.drawable.ic_launcher_background),
                painterResource(id = R.drawable.ic_launcher_background),
                painterResource(id = R.drawable.ic_launcher_background),
            ),
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
            images = listOf(
                painterResource(id = R.drawable.ic_launcher_background),
            ),
            onTextChanged = {},
            onChangeDate = {},
            onAttachFile = {})
    }
}