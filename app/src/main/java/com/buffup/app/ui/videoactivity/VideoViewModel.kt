package com.buffup.app.ui.videoactivity

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class VideoViewModel(
    private val context: Context
) : ViewModel() {
    private val player: ExoPlayer = SimpleExoPlayer.Builder(context).build()

    fun getPlayer(): Player = player

    fun initPlayer(url: String) {
        val userAgent = Util.getUserAgent(context, context.packageName)
        val mediaSource = ProgressiveMediaSource
            .Factory(
                DefaultDataSourceFactory(context, userAgent),
                DefaultExtractorsFactory()
            )
            .createMediaSource(Uri.parse(url))
        player.prepare(mediaSource)
    }

    fun play() {
        player.playWhenReady = true
    }

    fun pause() {
        player.playWhenReady = false
    }

    fun release() {
        player.release()
    }

}