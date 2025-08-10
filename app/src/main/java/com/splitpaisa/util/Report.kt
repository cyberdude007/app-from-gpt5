package com.splitpaisa.util

import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object ReportUtil {
    fun createSimpleReport(context: Context, title: String = "SplitPaisa Report"): File {
        val doc = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = doc.startPage(pageInfo)
        val c = page.canvas
        val paint = android.graphics.Paint().apply { textSize = 16f }
        c.drawText(title, 40f, 50f, paint)
        c.drawText("This is a summary report.", 40f, 80f, paint)
        doc.finishPage(page)

        val file = File(context.cacheDir, "report.pdf")
        doc.writeTo(file.outputStream())
        doc.close()
        return file
    }

    fun share(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share report"))
    }
}
