package com.buffup.app.ui.streamactivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.buffup.app.R
import com.buffup.app.model.StreamFinished
import com.buffup.app.model.StreamState
import com.buffup.app.model.ViewResult
import com.buffup.app.ui.streamactivity.streamlist.StreamAdapter
import com.buffup.app.ui.streamactivity.streamlist.StreamClickListener
import com.buffup.app.ui.streamactivity.streamlist.StreamListViewModel
import com.buffup.app.ui.videoactivity.VideoActivity
import com.buffup.app.utils.Constants
import kotlinx.android.synthetic.main.fragment_stream_list.*
import org.koin.android.ext.android.inject
import org.koin.core.KoinComponent

class StreamActivity : AppCompatActivity(),StreamClickListener, KoinComponent {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)
        initView()
        observeViewModel()
        viewModel.getStreamList()
    }

        private val viewModel: StreamListViewModel by inject()
        private val layoutManager = createLayoutManager()
        private lateinit var adapter: StreamAdapter


        override fun onPlayClick(streamState: StreamState) {
            if (streamState.url == null) {
                Toast.makeText(
                    this,
                    "Video is not attached to the stream",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                openVideoActivity(streamState)
            }
        }

        override fun onRemindClick(id: Int) {
            // TODO: Implement
        }

        override fun onStreamClick(id: Int) {
            // TODO: Open stream details
        }

        private fun initView() {
            refreshLayout.setOnRefreshListener {
                viewModel.getStreamList()
            }
            adapter = StreamAdapter(this, this)
            streamsRecyclerView.layoutManager = layoutManager
            streamsRecyclerView.adapter = adapter
        }

        private fun observeViewModel() {
            viewModel.streamsResult.observe(this, Observer { viewResult ->
                when (viewResult) {
                    is ViewResult.Loading -> onLoading()
                    is ViewResult.Content -> onSuccess(viewResult.content)
                    is ViewResult.Error -> onError(viewResult.error)
                }
            })
        }

        private fun onLoading() {
            refreshLayout.isRefreshing = true
        }

        private fun onSuccess(data: List<StreamState>) {
            refreshLayout.isRefreshing = false
            emptyLayout.visibility = if (data.isEmpty()) View.VISIBLE else View.GONE
            adapter.setItems(data)
        }

        private fun onError(error: Throwable?) {
            refreshLayout.isRefreshing = false
            emptyLayout.visibility = View.VISIBLE
            Log.w("StreamList", error)
            Toast.makeText(this, getString(R.string.data_error), Toast.LENGTH_LONG).show()
        }

        private fun createLayoutManager() =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        private fun openVideoActivity(streamState: StreamState) {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra(Constants.STREAM_ID, streamState.title)
            intent.putExtra(Constants.STREAM_URL, streamState.url)
            intent.putExtra(Constants.STREAM_VOD, streamState is StreamFinished)
            startActivity(intent)
        }

    }
