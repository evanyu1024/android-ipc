package com.evan.ipc.aidl

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author evanyu
 * @date 2018/9/27
 */
class AidlService : Service() {

    /**
     * CopyOnWriteArrayList 支持并发读写
     */
    private val bookList = CopyOnWriteArrayList<Book>()
    private val onBookListChangedListeners = RemoteCallbackList<OnBookListChangedListener>()

    /**
     * 创建 Binder 对象，实现 AIDL 自动生成的接口
     */
    private val binder = object : IBookManager.Stub() {
        override fun addBook(book: Book?) {
            if (book != null) {
                this@AidlService.bookList.add(book)
            }
            // 遍历 RemoteCallbackList 类型的集合
            val size = onBookListChangedListeners.beginBroadcast()
            for (i in 0 until size) {
                val listener = onBookListChangedListeners.getBroadcastItem(i)
                listener?.onChange()
            }
            onBookListChangedListeners.finishBroadcast()
        }

        override fun getBookList(): List<Book> = this@AidlService.bookList

        override fun addOnBookListChangedListener(listener: OnBookListChangedListener?) {
            val result = onBookListChangedListeners.register(listener)
            Log.d("mtag", "addOnBookListChangedListener -> register -> $result")
        }

        override fun removeOnBookListChangedListener(listener: OnBookListChangedListener?) {
            val result = onBookListChangedListeners.unregister(listener)
            Log.d("mtag", "removeOnBookListChangedListener -> unregister -> $result")
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("mtag", "AidlService#onBind")
        return binder
    }
}