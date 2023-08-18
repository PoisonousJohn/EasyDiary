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

package pro.fateev.diary

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.exifinterface.media.ExifInterface
import java.io.ByteArrayOutputStream
import kotlin.math.ceil
import kotlin.math.min


object ImageUtils {
    fun Uri.toEXIFAwareImageBytes(contentResolver: ContentResolver): ByteArray {
        var orientation: Int = ExifInterface.ORIENTATION_NORMAL
        contentResolver.openInputStream(this).use {
            if (it == null) error("Failed to open stream to ${this.path}")

            val exifInterface = ExifInterface(it)
            orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }

        contentResolver.openInputStream(this).use {
            if (it == null) error("Failed to open stream to ${this.path}")
            val bytes = it.buffered().readBytes()
            val bitmap = bytes.toBitmap()
            try {
                val matrix = Matrix()

                if (orientation != ExifInterface.ORIENTATION_NORMAL) {
                    matrix.preRotate(exifToDegrees(orientation).toFloat())
                    val rotatedBitmap =
                        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
                    val result = rotatedBitmap.toPNGByteArray()
                    rotatedBitmap.recycle()
                    return result
                }

                return bitmap.toPNGByteArray()
            } finally {
                bitmap.recycle()
            }
        }
    }

    fun ByteArray.toPainter(): BitmapPainter =
        toBitmap().asImageBitmap().let(::BitmapPainter)

    fun Bitmap.toPNGByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        if (!compress(Bitmap.CompressFormat.JPEG, 90, stream))
            error("Failed to compress stream")
        val compressedByteArray = stream.toByteArray()
        compressedByteArray.toBitmap()
        return compressedByteArray
    }

    fun exifToDegrees(exifOrientation: Int): Int = when (exifOrientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, size)
            ?: error("Failed to decode bitmap")
    }

    fun sliceInChunks(byteArray: ByteArray, chunkSizeBytes: Int): List<ByteArray> {
        val chunks = ceil(byteArray.size.toDouble() / chunkSizeBytes).toInt()
        val result = mutableListOf<ByteArray>()
        for (chunkIdx in 0 until chunks) {
            val offset = chunkIdx * chunkSizeBytes
            val bytesToCopy = min(chunkSizeBytes, byteArray.size - offset)
            val chunkData = ByteArray(bytesToCopy)
            byteArray.copyInto(chunkData, 0, offset, offset + bytesToCopy)
            result.add(chunkData)
        }

        return result
    }

    fun joinChunks(dataChunks: Array<ByteArray>) : ByteArray {
        val size = dataChunks.sumOf { it.size }
        val data = ByteArray(size)
        var offset = 0
        for (chunk in dataChunks) {
            chunk.copyInto(data, offset, 0, chunk.size)
            offset += chunk.size
        }
        return data
    }
}