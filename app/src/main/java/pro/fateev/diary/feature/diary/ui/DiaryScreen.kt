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

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.ImageUtils.toPainter
import pro.fateev.diary.extensions.FormattingExtensions.formatShort
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import pro.fateev.diary.ui.theme.body2Secondary
import java.util.Date

@Composable
fun DiaryEntryCard(entry: DiaryEntry) = Card(
    elevation = 4.dp, modifier = Modifier
        .padding(vertical = 8.dp)
        .fillMaxWidth()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = entry.text,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp),
        )
    }
}

@Composable
fun DiaryScreen(vm: DiaryScreenViewModel) {
    val entries: List<DiaryEntry> = vm.entries.collectAsState(initial = emptyList()).value
    DiaryScreenContent(entries, onAddEntry = vm::onAddEntry, onEditEntry = vm::onEditEntry)
}

@Composable
fun DiaryScreenContent(
    entries: List<DiaryEntry>,
    onAddEntry: () -> Unit = {},
    onEditEntry: (DiaryEntry) -> Unit = {}
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
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding), contentPadding = PaddingValues(12.dp)
            ) {
                items(entries.size) {
                    Column(
                        modifier = Modifier.clickable(onClick = { onEditEntry(entries[it]) })
                    ) {
                        val spacing = 12.dp
                        val entry = entries[it]
                        EntryHeader(index = it, date = entry.date)
                        if (entry.media.isNotEmpty()) {
                            LazyRow(
                                modifier = Modifier.padding(top = spacing),
                                horizontalArrangement = Arrangement.spacedBy(spacing)) {
                                items(entry.media.size) {
                                    val m = entry.media[it]
                                    val shape = RoundedCornerShape(spacing)
                                    Image(
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.requiredSize(150.dp).clip(shape).shadow(4.dp, shape).weight(1f),
                                        painter = m.data.toPainter(), contentDescription = ""
                                    )
                                }
                            }
                        }
                        DiaryEntryCard(entry)
                    }
                }
            }
        })
}

@Composable
fun ColumnScope.EntryHeader(index: Int, date: Date) {
    Row(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = if (index > 0) 16.dp else 0.dp)
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
@Preview
fun DiaryScreenPreview() {
    DiaryScreenContent(
        entries = listOf(
            DiaryEntry(id = 1, date = Date(), text = "Test asd;lfkj as;dlkfj a;sldkjf a;lskdfj"),
        )
    )
}
