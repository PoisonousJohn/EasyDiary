/*
 * Copyright 2021 Marco Cattaneo
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

package dev.marcocattaneo.androidcomposetemplate.ui.screen.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.marcocattaneo.androidcomposetemplate.ui.theme.body1Secondary
import dev.marcocattaneo.androidcomposetemplate.ui.theme.body2Secondary

data class DiaryEntry(val text: String)

@Composable
fun MainScreen(entries: List<DiaryEntry>) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                shape = RoundedCornerShape(10.dp),
                onClick = {},
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "icon")
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        isFloatingActionButtonDocked = true,
        content = { padding ->
            LazyColumn(
                modifier = Modifier.padding(padding),
                contentPadding = PaddingValues(12.dp)
            )
            {
                items(entries.size)
                {
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
        }
    )
}

@Composable
fun DiaryEntryCard(entry: DiaryEntry) =
    Card(
        elevation = 4.dp,
        modifier = Modifier
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
fun LoginScreen(
    loginViewModel: LoginViewModel
) {
    val username by loginViewModel.usernameState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = loginViewModel::onChangeUsername
        )
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = loginViewModel::onClickLogin) {
            Text(text = "Login")
        }
        FloatingActionButton(
            onClick = {
                Toast.makeText(context, "Test", Toast.LENGTH_SHORT).show()
            }
        ) {
            Icon(imageVector = Icons.Rounded.Add, tint = Color.White, contentDescription = "Add")
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(
        listOf(
            DiaryEntry("Test asd;lfkj as;dlkfj a;sldkjf a;lskdfj"),
            DiaryEntry("Test asdf;lkja sdf;lkj"),
            DiaryEntry("Test asdf;lkja sdf;lkj dsf;lkajsd f;laksj df;lkjas d;fklaj sd")
        )
    )
}