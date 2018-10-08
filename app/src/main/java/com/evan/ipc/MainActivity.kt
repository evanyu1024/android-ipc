package com.evan.ipc

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.evan.ipc.aidl.AidlActivity
import com.evan.ipc.binderpool.BinderPoolActivity
import com.evan.ipc.messenger.MessengerActivity
import com.evan.ipc.socket.SocketActivity
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("HandlerLeak")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_aidl.setOnClickListener {
            startActivity(Intent(this, AidlActivity::class.java))
        }

        btn_binder_pool.setOnClickListener {
            startActivity(Intent(this, BinderPoolActivity::class.java))
        }

        btn_messenger.setOnClickListener {
            startActivity(Intent(this, MessengerActivity::class.java))
        }

        btn_socket.setOnClickListener {
            startActivity(Intent(this, SocketActivity::class.java))
        }
    }
}
