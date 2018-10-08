package com.evan.ipc.messenger

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

/**
 * @author evanyu
 * @date 2018/9/28
 */
class MessengerService : Service() {

    private val messenger: Messenger = Messenger(MessengerHandler())

    override fun onBind(intent: Intent?): IBinder {
        return messenger.binder
    }

    companion object {
        private class MessengerHandler : Handler() {
            override fun handleMessage(msg: Message?) {
                if (msg != null) {
                    // 处理来自客户端的消息
                    Log.d("mtag", "server -> ${msg.data?.getString("info")}")
                    // 向客户端发送消息
                    val replyMsg = Message.obtain()
                    replyMsg.data = Bundle()
                    replyMsg.data.putString("info", "666")
                    // 向客户端回传消息，这里的 replyTo 是客户端提供的Messenger对象
                    msg.replyTo?.send(replyMsg)
                }
            }
        }
    }
}