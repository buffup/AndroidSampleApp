package com.buffup.app.utils

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.DisplayMetrics
import androidx.core.text.bold
import androidx.core.text.toSpannable
import com.buffup.app.model.StreamFinished
import com.buffup.app.model.StreamFuture
import com.buffup.app.model.StreamLive
import com.buffup.app.model.StreamState
import com.buffup.sdk.models.stream.Stream
import com.buffup.sdk.models.stream.StreamImage

class StreamUITransformer(
    private val displayMetrics: DisplayMetrics
) {

    fun convertToStreamState(stream: Stream): StreamState {
        val currentTime = System.currentTimeMillis()
        return when {
            currentTime in (stream.startAt + 1) until stream.endAt -> toStreamLive(stream)
            stream.startAt > currentTime -> toStreamFuture(stream)
            else -> toStreamFinished(stream)
        }
    }

    private fun toStreamFinished(stream: Stream) = StreamFinished(
        id = stream.id,
        title = stream.title,
        description = setDateDescription(stream),
        startAt = stream.startAt,
        endAt = stream.endAt,
        image = getImageUrl(stream.image),
        logo = getImageUrl(stream.logo),
        url = stream.url
    )

    private fun toStreamFuture(stream: Stream) = StreamFuture(
        id = stream.id,
        title = stream.title,
        description = setDateDescription(stream),
        startAt = stream.startAt,
        endAt = stream.endAt,
        image = getImageUrl(stream.image),
        logo = getImageUrl(stream.logo),
        dayOfWeek = DateUtils.getDayOfWeek(stream.startAt),
        url = stream.url
    )

    private fun toStreamLive(stream: Stream) = StreamLive(
        id = stream.id,
        title = stream.title,
        description = stream.description,
        startAt = stream.startAt,
        endAt = stream.endAt,
        image = getImageUrl(stream.image),
        logo = getImageUrl(stream.logo),
        url = stream.url
    )

    private fun getImageUrl(image: StreamImage?): String? {
        if (image == null)
            return null
        val density = displayMetrics.density
        return when {
            density <= 1 -> image.urlMdpi
            density <= 2 -> image.urlXhdpi
            else -> image.urlXxhdpi
        }
    }

    private fun setDateDescription(item: Stream): Spannable {
        return SpannableStringBuilder()
            .bold { append(item.description ?: "") }
            .append(" ·")
            .append(DateUtils.getDateDescription(item.startAt)).toSpannable()
    }

}