package com.evan.ipc.aidl

import android.os.RemoteCallbackList
import android.util.Log
import java.util.concurrent.CopyOnWriteArrayList

class BookManagerBinder : IBookManager.Stub() {

    /**
     * CopyOnWriteArrayList 支持并发读写
     */
    private val bookList = CopyOnWriteArrayList<Book>()
    private val onBookListChangedListeners = RemoteCallbackList<OnBookListChangedListener>()

    override fun addBook(book: Book?) {
        if (book != null) {
            this@BookManagerBinder.bookList.add(book)
        }
        // 遍历 RemoteCallbackList 类型的集合
        val size = onBookListChangedListeners.beginBroadcast()
        for (i in 0 until size) {
            val listener = onBookListChangedListeners.getBroadcastItem(i)
            listener?.onChange()
        }
        onBookListChangedListeners.finishBroadcast()
    }

    override fun getBookList(): List<Book> = this@BookManagerBinder.bookList

    override fun addOnBookListChangedListener(listener: OnBookListChangedListener?) {
        val result = onBookListChangedListeners.register(listener)
        Log.d("mtag", "addOnBookListChangedListener -> register -> $result")
    }

    override fun removeOnBookListChangedListener(listener: OnBookListChangedListener?) {
        val result = onBookListChangedListeners.unregister(listener)
        Log.d("mtag", "removeOnBookListChangedListener -> unregister -> $result")
    }
}