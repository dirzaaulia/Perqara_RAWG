package com.dirzaaulia.perqararawg.util

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.dirzaaulia.perqararawg.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.changeDateFormat(
    fromFormat: String,
    toFormat: String,
    fromTimeZone: String,
): String {
    val dateParser = SimpleDateFormat(fromFormat, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone(fromTimeZone)
    }
    val date: Date?

    return try {
        date = dateParser.parse(this)
        val dateFormatter = SimpleDateFormat(toFormat, Locale.getDefault())

        date?.let { dateFormatter.format(it) }.replaceIfNull()
    } catch (exception: Exception) {
        exception.printStackTrace()
        ""
    }
}

fun String?.replaceIfNull(replacementValue: String = ""): String {
    if (this == null)
        return replacementValue
    return this
}

fun Int?.replaceIfNull(replacementValue: Int = 0): Int {
    if (this == null)
        return replacementValue
    return this
}

fun ImageView.loadNetworkImage(url: String) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.setColorSchemeColors(
        ContextCompat.getColor(this.context, R.color.md_theme_dark_onSurfaceVariant)
    )
    circularProgressDrawable.start()

    this.load(url) {
        crossfade(true)
        placeholder(circularProgressDrawable)
    }
}