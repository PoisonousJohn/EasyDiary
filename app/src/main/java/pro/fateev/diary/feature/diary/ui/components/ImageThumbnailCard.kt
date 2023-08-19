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

package pro.fateev.diary.feature.diary.ui.components

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun ImageThumbnailCard(size: Dp, path: String, modifier: Modifier) {
    val shape = RoundedCornerShape(12.dp)
    Card(elevation = 12.dp, shape = shape) {
        val imageRequest = ImageRequest.Builder(LocalContext.current)
            .size(with(LocalDensity.current) { size.roundToPx() })
            .data(path)
            .crossfade(true)
            .build()
        AsyncImage(
            model = imageRequest,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .requiredSize(size)
                .clip(shape),
            contentDescription = ""
        )

    }
}
