package com.evan.ipc.messenger

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.evan.ipc.R
import kotlinx.android.synthetic.main.activity_messenger.*

/**
 * @author evanyu
 * @date 2018/9/29
 */
class MessengerActivity : AppCompatActivity() {

    /**
     * 用于向服务端发送消息的 Messenger
     */
    private var messenger: Messenger? = null
    /**
     * 用于服务端回传消息的 Messenger
     */
    private val replyMessenger = Messenger(MessengerHandler())

    /**
     * 和服务端的连接对象
     */
    private var serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.d("mtag", "onServiceConnected")
            // 和服务端成功建立连接后，创建 Messenger 对象
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("mtag", "onServiceDisconnected")
            messenger = null
        }
    }

    companion object {
        class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message?) {
                if (msg != null) {
                    // 处理来自服务端的消息
                    Log.d("mtag", "client -> ${msg.data.getString("info")}")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenger)

        btn_send_message.setOnClickListener {
            if (messenger != null) {
                // 向服务端发送消息
                val msg = Message.obtain()
                msg.data = Bundle()
                msg.data.putString("info", et_content.text.toString())
                // 设置服务端回传消息时使用的 Messenger 对象
                msg.replyTo = replyMessenger
                messenger?.send(msg)
            } else {
                Log.d("mtag", "send message failed: messenger = null")
            }
        }

        // 绑定远程服务
        val intent = Intent(this, MessengerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        // 断开与服务端的连接
        unbindService(serviceConnection)
    }
}