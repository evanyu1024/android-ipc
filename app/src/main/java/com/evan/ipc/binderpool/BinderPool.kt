package com.evan.ipc.binderpool

import android.os.IBinder
import com.evan.ipc.aidl.BookManagerBinder

class BinderPool : IBinderPool.Stub() {

    companion object {
        const val BINDER_CODE_COMPUTE = 1
        const val BINDER_CODE_BOOK_MANAGER = 2
    }

    override fun queryBinder(binderCode: Int): IBinder? {
        return when (binderCode) {
            BINDER_CODE_COMPUTE -> ComputeBinder()
            BINDER_CODE_BOOK_MANAGER -> BookManagerBinder()
            else -> null
        }
    }
}