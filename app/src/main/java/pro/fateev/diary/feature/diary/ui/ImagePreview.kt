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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import pro.fateev.R
import pro.fateev.diary.ImageUtils.toPainter

@Composable
fun ImagePreview(vm: ImagePreviewViewModel) {
    val data = vm.image.collectAsState(null).value ?: return
    ImagePreview(image = data.toPainter())
}

@Composable
fun ImagePreview(image: Painter) {
    val zoomState = rememberZoomState(contentSize = image.intrinsicSize)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .zoomable(zoomState)
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = image, contentDescription = ""
        )

    }
}

@Preview
@Composable
fun ImagePreviewPreview() {
    ImagePreview(image = painterResource(id = R.drawable.ic_launcher_background))
}