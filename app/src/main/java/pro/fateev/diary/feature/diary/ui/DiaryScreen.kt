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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.feature.diary.domain.model.DiaryEntry
import pro.fateev.diary.ui.theme.body2Secondary

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
            overflow = TextOverflow.Ellipsis,
            maxLines = 3
        )
    }
}

@Composable
fun DiaryScreen(vm: DiaryScreenViewModel) {
    val entries: List<DiaryEntry> = vm.entries.collectAsState(initial = emptyList()).value
    DiaryScreenContent(entries, onAddEntry = vm::onAddEntry)
}

@Composable
fun DiaryScreenContent(entries: List<DiaryEntry>, onAddEntry: () -> Unit) {
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
                    Column {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = if (it > 0) 12.dp else 0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.DateRange,
                                contentDescription = "Date",
                                tint = MaterialTheme.typography.body2Secondary.color
                            )
                            Text(
                                "23.12",
                                style = MaterialTheme.typography.body2Secondary,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                        }
                        DiaryEntryCard(entries[it])
                    }
                }
            }
        })
}

@Composable
@Preview
fun MainScreenPreview() {
    DiaryScreenContent(
        listOf(
            DiaryEntry("Test asd;lfkj as;dlkfj a;sldkjf a;lskdfj"),
            DiaryEntry("Test asdf;lkja sdf;lkj"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd"),
        ), {}
    )
}
