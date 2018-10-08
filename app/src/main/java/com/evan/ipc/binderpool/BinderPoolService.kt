package com.evan.ipc.binderpool

import android.app.Service
import android.content.Intent
import android.os.IBinder

class BinderPoolService : Service() {

    override fun onBind(intent: Intent): IBinder {
        return BinderPool()
    }
}
