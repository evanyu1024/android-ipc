package com.evan.ipc.socket

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.evan.ipc.R
import kotlinx.android.synthetic.main.activity_socket.*
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket
import java.util.concurrent.Executors

class SocketActivity : AppCompatActivity() {

    private var tcpSocket: Socket? = null

    companion object {
        private var udpSocket: DatagramSocket? = null
        private val EXECUTOR = Executors.newFixedThreadPool(5)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)

        startService(Intent(this, SocketService::class.java))

        btn_socket_send_message_tcp.setOnClickListener { sendMessageByTCP() }
        btn_socket_send_message_udp.setOnClickListener { sendMessageByUDP() }
    }

    @SuppressLint("SetTextI18n")
    private fun sendMessageByTCP() {
        // 网络操作必须在子线程完成
        EXECUTOR.execute {
            // 创建 tcp Socket对象，设置服务端的ip和端口
            if (tcpSocket == null) {
                tcpSocket = Socket("127.0.0.1", 6666)
            }
            val br = BufferedReader(InputStreamReader(tcpSocket?.getInputStream()))
            val bw = BufferedWriter(OutputStreamWriter(tcpSocket?.getOutputStream()))
            // 向服务端发送消息
            bw.write(et_socket_content.text.toString())
            bw.newLine()
            bw.flush()
            // 接收来自服务端的消息
            val reply = br.readLine()
            Log.d("mtag", reply)
            runOnUiThread { tv_socket_reply_message.text = "tcp: $reply" }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun sendMessageByUDP() {
        EXECUTOR.execute {
            // 监听 10000 端口
            if (udpSocket == null) {
                udpSocket = DatagramSocket(10000)
            }
            // 发送消息
            val content = et_socket_content.text.toString().toByteArray()
            val packet = DatagramPacket(content, content.size, InetAddress.getLocalHost(), 10086)
            udpSocket?.send(packet)
            // 接收消息
            val responseBuffer = ByteArray(1024)
            val responsePacket = DatagramPacket(responseBuffer, responseBuffer.size)
            udpSocket?.receive(responsePacket)
            runOnUiThread {
                tv_socket_reply_message.text = "udp: ${String(responseBuffer, 0, responsePacket.length)}"
            }
        }
    }
}
