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

package pro.fateev.diary.feature.diary.ui

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.ImageUtils.pathToImageRequest
import pro.fateev.diary.extensions.FormattingExtensions.formatShort
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import pro.fateev.diary.feature.diary.domain.model.Media
import pro.fateev.diary.feature.diary.ui.components.ImageThumbnailCard
import pro.fateev.diary.feature.diary.ui.components.MyTopAppBar
import pro.fateev.diary.ui.theme.AppTheme
import pro.fateev.diary.ui.theme.body2Secondary
import java.util.Date

@Composable
fun DiaryEntryCard(entry: DiaryEntry, onImageClick: (Int) -> Unit = {}) = Card(
    elevation = 4.dp, modifier = Modifier
        .fillMaxWidth()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        val contentPadding = 8.dp
        EntryHeader(date = entry.date, modifier = Modifier.padding(vertical = contentPadding))
        if (entry.media.isNotEmpty()) {
            val spacing = 12.dp
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(spacing),
                contentPadding = PaddingValues(horizontal = contentPadding)
            ) {
                val size = 150.dp
                items(entry.media.size) { mediaIndex ->
                    val m = entry.media[mediaIndex]
                    ImageThumbnailCard(
                        size = size,
                        data = m.pathToFile.pathToImageRequest().build(),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { onImageClick.invoke(mediaIndex) },
                    )
                }
            }
        }
        Text(
            text = entry.text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxWidth()
        )
    }
}

@Composable
fun DiaryScreen(vm: DiaryScreenViewModel) {
    val entries: List<DiaryEntry> = vm.entries.collectAsState(initial = emptyList()).value
    DiaryScreenContent(
        entries,
        onAddEntry = vm::onAddEntry,
        onEditEntry = vm::onEditEntry,
        onImageClick = vm::onImageClick,
        onSettingsClicked = vm::onSettingsClicked
    )
}

@Composable
fun DiaryScreenContent(
    entries: List<DiaryEntry>,
    onAddEntry: () -> Unit = {},
    onEditEntry: (DiaryEntry) -> Unit = {},
    onImageClick: (Media) -> Unit = {},
    onSettingsClicked: () -> Unit = {}
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(10.dp),
                onClick = onAddEntry,
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        topBar = {
            MyTopAppBar(actions = {
                IconButton(onClick = onSettingsClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Settings"
                    )
                }
            })
        },
        content = { padding ->
            val cardPadding = 12.dp
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(top = cardPadding)
                    .background(MaterialTheme.colors.background),
                verticalArrangement = Arrangement.spacedBy(cardPadding)
            ) {
                items(entries.size) { entryIndex ->
                    Column(
                        modifier = Modifier.clickable(onClick = { onEditEntry(entries[entryIndex]) })
                    ) {
                        val entry = entries[entryIndex]
                        DiaryEntryCard(
                            entry
                        ) { mediaIndex -> onImageClick.invoke(entries[entryIndex].media[mediaIndex]) }
                    }
                }
            }
        })
}

@Composable
fun ColumnScope.EntryHeader(date: Date, modifier: Modifier) {
    Row(
        modifier = modifier.align(Alignment.CenterHorizontally)
    ) {
        Icon(
            imageVector = Icons.Rounded.DateRange,
            contentDescription = "Date",
            tint = MaterialTheme.typography.body2Secondary.color
        )
        Text(
            date.formatShort(LocalContext.current),
            style = MaterialTheme.typography.body2Secondary,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }

}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
fun DiaryScreenPreview() {
    AppTheme {
        DiaryScreenContent(
            entries = listOf(
                DiaryEntry(id = 1, date = Date(), text = "One line\ntwo line\nthree line"),
                DiaryEntry(id = 1, date = Date(), text = "One line\ntwo line\nthree line"),
                DiaryEntry(id = 1, date = Date(), text = "One line\ntwo line\nthree line"),
            )
        )
    }
}
