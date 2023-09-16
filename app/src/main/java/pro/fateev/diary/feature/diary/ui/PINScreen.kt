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
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.Circle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.ui.theme.AppTheme
import pro.fateev.diary.ui.theme.neutralBackground
import pro.fateev.diary.ui.theme.text

enum class PINError {
    WrongPIN,
    PINDoesNotMatch
}

sealed class PINStatus {
    data class Error(val error: PINError) : PINStatus()
    object Success : PINStatus()
    data class Filled(val count: Int) : PINStatus()
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PINScreenPreview() {
    AppTheme() {
        PINScreen()
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PINScreenPreviewFilled() {
    AppTheme() {
        PINScreen(pinStatus = PINStatus.Filled(3))
    }
}


@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PINScreenPreviewError() {
    AppTheme() {
        PINScreen(pinStatus = PINStatus.Error(PINError.WrongPIN))
    }
}

@Composable
fun PINScreen(vm: PINScreenViewModel) {
    val pinStatus = vm.pinStatus.collectAsState(initial = PINStatus.Filled(0))
    val mode = vm.mode.collectAsState(initial = PINScreenViewModel.Mode.Auth).value
    PINScreen(
        when (mode) {
            PINScreenViewModel.Mode.Auth -> "Enter PIN"
            PINScreenViewModel.Mode.SetPIN -> "Set PIN"
            PINScreenViewModel.Mode.RepeatPIN -> "Repeat PIN"
        },
        pinLength = vm.pinLength,
        onNumberClicked = vm::onEnterNumber,
        onBackspaceClicked = vm::onBackspace,
        pinStatus = pinStatus.value
    )
}

@Composable
fun PINScreen(
    title: String = "Enter PIN",
    onNumberClicked: (Int) -> Unit = {},
    onBackspaceClicked: () -> Unit = {},
    pinLength: Int = 5,
    pinStatus: PINStatus = PINStatus.Filled(0)
) {
    // can not close the screen
    BackHandler(enabled = true) { }
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {


            val spacing = 8.dp
            val spacingAroundDots = 40.dp
            Text(
                text = title, style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(bottom = spacingAroundDots),
            )
            if (pinStatus is PINStatus.Error) {
                val errorText = when (pinStatus.error) {
                    PINError.WrongPIN -> "Incorrect PIN"
                    PINError.PINDoesNotMatch -> "PIN does not match"
                }
                Text(
                    text = errorText,
                    style = MaterialTheme.typography.body1.merge(MaterialTheme.colors.error),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing),
                modifier = Modifier.padding(bottom = spacingAroundDots)
            ) {
                for (i in 1..pinLength) {
                    val tint = when (pinStatus) {
                        is PINStatus.Error -> MaterialTheme.colors.error
                        is PINStatus.Filled -> if (i <= pinStatus.count) MaterialTheme.colors.primary else MaterialTheme.colors.neutralBackground
                        PINStatus.Success -> MaterialTheme.colors.primary
                    }
                    Image(
                        imageVector = Icons.Default.Circle,
                        contentDescription = "pin item",
                        colorFilter = ColorFilter.tint(tint)
                    )
                }
            }

            val buttonSize = 66.dp
            LazyVerticalGrid(
                columns = GridCells.FixedSize(buttonSize),
                modifier = Modifier.width(buttonSize * 1.1f * 3),
                horizontalArrangement = Arrangement.spacedBy(
                    spacing,
                    alignment = Alignment.CenterHorizontally
                ),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                val itemsCount = 11
                fun isZeroButton(index: Int) = index == 9
                items(itemsCount, span = {
                    GridItemSpan(if (isZeroButton(it)) 2 else 1)
                }) {
                    val isNumberKey = it <= 9
                    val alignment: Alignment =
                        if (isZeroButton(it)) Alignment.CenterEnd else Alignment.Center
                    Box(
                        contentAlignment = alignment,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 4.dp)
                    ) {
                        if (isNumberKey) {
                            Button(
                                onClick = { onNumberClicked.invoke(it + 1) },
                                shape = CircleShape,
                                modifier = Modifier.size(buttonSize),
                                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.neutralBackground)
                            ) {
                                Text(
                                    text = "${(it + 1) % 10}",
                                    style = MaterialTheme.typography.body1
                                )
                            }
                        } else {
                            IconButton(
                                onClick = onBackspaceClicked,
                                modifier = Modifier.size(buttonSize)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Backspace,
                                    modifier = Modifier.size(buttonSize * 0.8f),
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colors.text
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
