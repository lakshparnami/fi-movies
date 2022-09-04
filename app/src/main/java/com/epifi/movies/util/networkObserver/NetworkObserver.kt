package com.epifi.movies.util.networkObserver

import android.content.Context
import androidx.lifecycle.LifecycleOwner

interface NetworkObserver {
    fun registerNetworkObserver(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        onAvailable: () -> Unit,
        onLost: () -> Unit,
    )
}