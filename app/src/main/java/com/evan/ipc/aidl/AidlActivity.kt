package com.evan.ipc.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.evan.ipc.R
import kotlinx.android.synthetic.main.activity_aidl.*

class AidlActivity : AppCompatActivity() {

    private var remoteService: IBookManager? = null

    /**
     * Binder 的死亡代理
     * 当 Binder 死亡时，会回调 binderDied() 方法。
     */
    private val deathRecipient: IBinder.DeathRecipient? by lazy {
        IBinder.DeathRecipient {
            remoteService?.asBinder()?.unlinkToDeath(deathRecipient, 0)
            remoteService = null
            // 这里可以重新绑定远程 Service
            bindAidlService()
        }
    }

    private val serviceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("mtag", "onServiceConnected")
            remoteService = IBookManager.Stub.asInterface(service)
            remoteService?.addOnBookListChangedListener(onBookListChangedListener)
            /*
             * 在成功绑定远程服 Service 后，为 Binder 设置死亡代理
             * Binder 的 isBinderAlive 也可以判断 Binder 是否死亡
             */
            service?.linkToDeath(deathRecipient, 0)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("mtag", "onServiceDisconnected")
            // TODO 也可以在这里重新绑定服务
        }
    }

    private val onBookListChangedListener = object : OnBookListChangedListener.Stub() {
        override fun onChange() {
            Log.d("mtag", "book list changed -> book.size=${remoteService?.bookList?.size}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl)

        btn_add_book.setOnClickListener {
            remoteService?.addBook(Book("好书", 66.6))
        }

        btn_get_book_list.setOnClickListener {
            Log.d("mtag", "bookList.size()=${remoteService?.bookList?.size}")
        }

        bindAidlService()
    }

    private fun bindAidlService() {
        val intent = Intent(this, AidlService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        remoteService?.removeOnBookListChangedListener(onBookListChangedListener)
        remoteService?.asBinder()?.unlinkToDeath(deathRecipient, 0)
        remoteService = null
        unbindService(serviceConnection)
    }
}
