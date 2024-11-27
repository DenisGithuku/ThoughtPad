
/*
* Copyright 2024 Denis Githuku
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* https://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.gitsoft.thoughtpad.feature.notelist.utils

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import timber.log.Timber

private const val ExceptionTag = "NoteSharingException"

fun Context.shareAsPdf(
    noteTitle: String? = null,
    noteText: String? = null,
    checklist: List<Pair<Boolean, String>> = emptyList()
) {
    /** Create a new PDF document */
    val pdfDocument = PdfDocument()

    /** Define page size (A4 size) */
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    /** Get reference to a canvas which we'll use to paint on */
    val canvas = page.canvas
    val paint = Paint()

    /** Draw the note title */
    noteTitle?.let {
        paint.textSize = 24f
        paint.isFakeBoldText = true
        canvas.drawText(it, 40f, 80f, paint)
    }

    var yPosition = 120f

    /** Draw the note content */
    noteText?.let {
        paint.textSize = 16f
        paint.isFakeBoldText = false
        val textLines = noteText.split("\n")
        for (line in textLines) {
            canvas.drawText(line, 40f, yPosition, paint)
            yPosition += 20f
        }
    }

    /** Draw checklist if provided */
    if (checklist.isNotEmpty()) {
        yPosition += 40f
        paint.textSize = 16f
        canvas.drawText("Checklist:", 40f, yPosition, paint)
        yPosition += 30f
        paint.textSize = 16f
        checklist.forEach { (isChecked, text) ->
            val checkbox = if (isChecked) "[✔]" else "[]"
            canvas.drawText("$checkbox $text", 40f, yPosition, paint)
            yPosition += 20f
        }
    }

    /** Finish the page */
    pdfDocument.finishPage(page)

    /** Save the file temporarily to the device */
    val tempFile = File(cacheDir, "note${System.currentTimeMillis()}.pdf")
    try {
        FileOutputStream(tempFile).use { pdfDocument.writeTo(it) }
    } catch (e: Exception) {
        e.printStackTrace()
        Timber.tag(ExceptionTag).e(e)
    } finally {
        pdfDocument.close()
    }

    /** Share the file */
    val pdfUri =
        FileProvider.getUriForFile(this, "${applicationContext.packageName}.fileProvider", tempFile)

    /** Create share intent */
    val shareIntent =
        Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, pdfUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    startActivity(Intent.createChooser(shareIntent, "Share Note"))
}
