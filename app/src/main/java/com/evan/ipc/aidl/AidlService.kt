package com.evan.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

/**
 * @author evanyu
 * @date 2018/9/27
 */
class AidlService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        Log.d("mtag", "AidlService#onBind")
        return BookManagerBinder()
    }
}