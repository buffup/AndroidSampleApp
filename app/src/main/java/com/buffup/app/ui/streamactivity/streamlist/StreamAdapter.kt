package com.buffup.app.ui.streamactivity.streamlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.buffup.app.R
import com.buffup.app.model.StreamFinished
import com.buffup.app.model.StreamFuture
import com.buffup.app.model.StreamLive
import com.buffup.app.model.StreamState
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_stream_future.view.*
import kotlinx.android.synthetic.main.item_stream_future.view.streamImage
import kotlinx.android.synthetic.main.item_stream_future.view.streamLogo
import kotlinx.android.synthetic.main.item_stream_future.view.streamStateText
import kotlinx.android.synthetic.main.item_stream_live.view.*
import kotlinx.android.synthetic.main.item_stream_live.view.emptyImageLayout
import kotlinx.android.synthetic.main.item_stream_live.view.streamDescriptionText
import kotlinx.android.synthetic.main.item_stream_live.view.streamTitleText

class StreamAdapter(
    context: Context,
    private val clickListener: StreamClickListener
) : RecyclerView.Adapter<StreamAdapter.StreamHolder>() {

    private val picasso = Picasso.get()
    private val items = mutableListOf<StreamState>()
    private val logoHeight = context.resources.getDimensionPixelSize(R.dimen.logo_height)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamAdapter.StreamHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            R.layout.item_stream_live -> LiveStreamHolder(itemView)
            R.layout.item_stream_future -> FutureStreamHolder(itemView)
            else -> FinishedStreamHolder(itemView)
        }
    }

    override fun onBindViewHolder(streamHolder: StreamHolder, position: Int) {
        streamHolder.bind(items[position])
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is StreamLive -> R.layout.item_stream_live
            is StreamFinished -> R.layout.item_stream_finished
            is StreamFuture -> R.layout.item_stream_future
        }
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<StreamState>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    abstract inner class StreamHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        open fun bind(item: StreamState) = with(itemView) {
            streamTitleText.text = item.title
            setOnClickListener {
                clickListener.onStreamClick(item.id)
            }

            if (item.image == null) {
                emptyImageLayout.visibility = VISIBLE
            } else {
                picasso.load(item.image)
                    .into(streamImage, object : Callback {
                        override fun onSuccess() {
                            emptyImageLayout.visibility = INVISIBLE
                        }

                        override fun onError(e: Exception?) {
                            emptyImageLayout.visibility = VISIBLE
                        }
                    })
            }

            picasso.load(item.logo)
                .resize(0, logoHeight)
                .into(streamLogo)
        }

    }

    inner class LiveStreamHolder(itemView: View) : StreamHolder(itemView) {

        override fun bind(item: StreamState) {
            super.bind(item)
            with(itemView) {
                setOnClickListener {
                    clickListener.onPlayClick(item)
                }

                with(item as StreamLive) {
                    streamDescriptionText.text = item.description
                }
            }
        }
    }

    inner class FutureStreamHolder(itemView: View) : StreamHolder(itemView) {

        override fun bind(item: StreamState) {
            super.bind(item)
            with(itemView) {
                remindButton.setOnClickListener {
                    clickListener.onRemindClick(item.id)
                }
                with(item as StreamFuture) {
                    streamDescriptionText.text = item.description
                    streamStateText.text = item.dayOfWeek
                }
            }
        }
    }

    inner class FinishedStreamHolder(itemView: View) : StreamHolder(itemView) {

        override fun bind(item: StreamState) {
            super.bind(item)
            with(item as StreamFinished) {
                itemView.streamDescriptionText.text = item.description
                itemView.setOnClickListener {
                    clickListener.onPlayClick(item)
                }
            }
        }
    }
}

interface StreamClickListener {
    fun onStreamClick(id: Int)
    fun onPlayClick(streamState: StreamState)
    fun onRemindClick(id: Int)
}