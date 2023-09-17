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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.fateev.diary.ui.theme.AppTheme

@Composable
fun YesNoScreen(
    title: String = "",
    description: String = "",
    positiveText: String = "Yes",
    negativeText: String = "No",
    onPositive: () -> Unit = {},
    onNegative: () -> Unit = {}
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val contentPadding = 12.dp
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = contentPadding,
                    start = contentPadding,
                    end = contentPadding,
                    bottom = contentPadding * 4
                )
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h2,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center
                )
            }
            val buttonModifier = Modifier
                .defaultMinSize(minWidth = 200.dp)
                .align(CenterHorizontally)
            TextButton(onClick = onNegative, modifier = buttonModifier) {
                Text(text = negativeText)
            }
            Button(onClick = onPositive, modifier = buttonModifier) {
                Text(text = positiveText)
            }
        }

    }
}

@Preview(uiMode = UI_MODE_NIGHT_NO)
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
fun PreviewYesNoScreenLight() {
    AppTheme() {
        YesNoScreen(title = "Test title", description = "Test description")
    }
}