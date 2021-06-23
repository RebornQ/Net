package com.drake.net.callback

import android.view.View
import com.drake.net.Net
import com.drake.net.NetConfig
import com.drake.net.interfaces.NetCallback
import com.drake.net.request.group
import com.drake.statelayout.StateLayout
import okhttp3.Call
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.CancellationException

abstract class StateCallback<T>(val state: StateLayout) : NetCallback<T>() {

    override fun onStart(request: Request) {
        super.onStart(request)
        state.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View) {
                Net.cancelGroup(request.group())
            }
        })
    }

    override fun onFailure(call: Call, e: IOException) {
        state.showError(e)
        super.onFailure(call, e)
    }

    override fun onError(call: Call, e: IOException) {
        NetConfig.errorHandler.onStateError(e, state)
        super.onError(call, e)
    }

    override fun onComplete(call: Call, e: IOException?) {
        super.onComplete(call, e)
        if (e == null || e is CancellationException) state.showContent()
    }
}