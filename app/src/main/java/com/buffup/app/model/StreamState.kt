package com.buffup.app.model

import android.text.Spannable

sealed class StreamState {
    abstract val id: Int
    abstract val title: String
    abstract val url: String?
    abstract val startAt: Long
    abstract val endAt: Long
    abstract val image: String?
    abstract val logo: String?
}

data class StreamLive(
    override val id: Int,
    override val title: String,
    override val url: String?,
    override val startAt: Long,
    override val endAt: Long,
    override val image: String?,
    override val logo: String?,
    val description: String?
) : StreamState()

data class StreamFinished(
    override val id: Int,
    override val title: String,
    override val url: String?,
    override val startAt: Long,
    override val endAt: Long,
    override val image: String?,
    override val logo: String?,
    val description: Spannable
) : StreamState()

data class StreamFuture(
    override val id: Int,
    override val title: String,
    override val url: String?,
    override val startAt: Long,
    override val endAt: Long,
    override val image: String?,
    override val logo: String?,
    val description: Spannable,
    val dayOfWeek: String
) : StreamState()
