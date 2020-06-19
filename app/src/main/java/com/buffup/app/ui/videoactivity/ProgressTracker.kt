package com.buffup.app.ui.videoactivity

import android.os.Handler
import com.google.android.exoplayer2.Player

interface PositionListener {
    fun progress(position: Long)
}

class ProgressTracker constructor(
    private val player: Player,
    private val positionListener: PositionListener
) : Runnable {
    private val handler: Handler = Handler()

    init {
        handler.post(this)
    }

    override fun run() {
        val position = player.currentPosition
        positionListener.progress(position)
        handler.postDelayed(this, DELAY_MS)
    }

    fun unsubscribe() {
        handler.removeCallbacks(this)
    }

    companion object {
        private const val DELAY_MS = 1000L
    }
}