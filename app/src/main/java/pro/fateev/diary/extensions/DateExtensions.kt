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

package pro.fateev.diary.extensions

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import java.util.Calendar
import java.util.Date

object DateExtensions {
    fun Date.toCalendar() = Calendar.getInstance().apply { time = this@toCalendar }
    fun Date.showDatePicker(context: Context, onDateSet: (Date) -> Unit) {
        val calendar = toCalendar()
        val picker = DatePickerDialog(
            context,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                onDateSet.invoke(Calendar.getInstance().apply { set(year, month, day) }.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH))
        picker.show()
    }
}