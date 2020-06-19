package com.buffup.app.ui.streamactivity.streamlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buffup.app.model.StreamState
import com.buffup.app.model.ViewResult
import com.buffup.app.utils.StreamUITransformer
import com.buffup.sdk.BuffSdk
import com.buffup.sdk.models.stream.Stream
import com.buffup.sdk.models.stream.StreamListResultListener

class StreamListViewModel(
    private val streamUITransformer: StreamUITransformer
) : ViewModel() {

    private val _streamsResult = MutableLiveData<ViewResult<List<StreamState>>>()
    val streamsResult: LiveData<ViewResult<List<StreamState>>> = _streamsResult

    fun getStreamList() {
        _streamsResult.value = ViewResult.Loading
        BuffSdk.streamProvider.getStreamList(object : StreamListResultListener {

            override fun onError(t: Throwable?) {
                _streamsResult.value = ViewResult.Error(t)
            }

            override fun onSuccess(data: List<Stream>) {
                _streamsResult.value =
                    ViewResult.Content(data
                        .map { streamUITransformer.convertToStreamState(it) })
            }
        })
    }

}
