package com.evan.ipc.socket

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.io.*
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.ServerSocket

class SocketService : Service() {

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        Thread(TcpServer()).start()
        Thread(UdpServer()).start()
    }

    inner class TcpServer : Runnable {
        override fun run() {
            // 创建服务端 Socket 对象，并设置监听接口
            val serverSocket = ServerSocket(6666)
            // 接收客户端的连接请求
            while (true) {
                val clientSocket = serverSocket.accept()
                Log.d("mtag", "accept client: ${clientSocket.inetAddress.hostAddress}")
                Thread {
                    Log.d("mtag", "server run on sub thread")
                    val br = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
                    val bw = BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream()))
                    var line: String?
                    while (true) {
                        // 接收来自客户端的消息
                        line = br.readLine()
                        Log.d("mtag", "server has received msg: $line")
                        if (line == null) {
                            break
                        }
                        // 向客户端发送消息
                        bw.write("server -> $line")
                        bw.newLine()
                        bw.flush()
                    }
                    clientSocket.close()
                }.start()
            }
        }
    }

    inner class UdpServer : Runnable {
        override fun run() {
            // 监听 10086 端口
            val socket = DatagramSocket(10086)

            val receivedBuffer = ByteArray(1024)
            val receivedPacket = DatagramPacket(receivedBuffer, receivedBuffer.size)
            val responsePacket = DatagramPacket(ByteArray(0), 0, InetAddress.getLocalHost(), 10000)

            var content: String?
            do {
                // 接收消息
                socket.receive(receivedPacket)
                content = String(receivedBuffer, 0, receivedPacket.length)
                Log.d("mtag", "server -> $content")
                // 发送消息
                val responseData = "server -> $content".toByteArray()
                responsePacket.setData(responseData, 0, responseData.size)
                socket.send(responsePacket)
            } while (!"over".equals(content, true)) // 当接收到的消息内容为 over 时，停止接收消息
            Log.d("mtag", "server is over")
        }
    }
}