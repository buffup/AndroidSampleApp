package com.buffup.app.ui.videoactivity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.buffup.app.R
import com.buffup.app.utils.Constants.STREAM_ID
import com.buffup.app.utils.Constants.STREAM_URL
import com.buffup.app.utils.Constants.STREAM_VOD
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_video.*
import org.koin.core.KoinComponent


class VideoActivity : AppCompatActivity(), KoinComponent, PositionListener {

    private lateinit var videoProgressTracker: ProgressTracker
    private lateinit var player: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.FullscreenTheme)
        setContentView(R.layout.activity_video)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        handleIntent()
    }

    override fun onResume() {
        super.onResume()
        player.playWhenReady = true
    }

    override fun onPause() {
        player.playWhenReady = false
        super.onPause()
    }

    override fun onStop() {
        videoProgressTracker.unsubscribe()
        player.release()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        buffView.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleIntent() {
        val bundle = intent.extras
        kotlin.runCatching {
            val id = bundle?.getString(STREAM_ID)
            val url = bundle?.getString(STREAM_URL)
            val isVod = bundle?.getBoolean(STREAM_VOD)
            buffView.init(id!!, isVod!!)
            setVideoPlayer(url!!)
        }.onFailure {
            finish()
        }
    }

    private fun setVideoPlayer(url: String) {
        player = ExoPlayerFactory.newSimpleInstance(this)

        val userAgent = Util.getUserAgent(this, packageName)
        val mediaSource = ExtractorMediaSource(
            Uri.parse(url),
            DefaultDataSourceFactory(this, userAgent),
            DefaultExtractorsFactory(), null, null
        )
        player.prepare(mediaSource)
        playerView.keepScreenOn = true

        playerView.player = player

        videoProgressTracker = ProgressTracker(player, this)
    }

    override fun progress(position: Long) {
        buffView.setVideoProgress(position)
    }
}
