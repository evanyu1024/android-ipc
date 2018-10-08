package com.evan.ipc.binderpool

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.evan.ipc.R
import com.evan.ipc.aidl.Book
import com.evan.ipc.aidl.IBookManager
import kotlinx.android.synthetic.main.activity_binder_pool.*

class BinderPoolActivity : AppCompatActivity() {

    private var binderPool: IBinderPool? = null
    private var computeBinder: ICompute? = null
    private var bookManagerBinder: IBookManager? = null

    private val serviceConnection: ServiceConnection? = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            binderPool = IBinderPool.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            binderPool = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_binder_pool)

        bindService(Intent(this, BinderPoolService::class.java), serviceConnection, Context.BIND_AUTO_CREATE)
    }

    @SuppressLint("SetTextI18n")
    fun getSum(view: View) {
        if (computeBinder == null) {
            computeBinder = ICompute.Stub.asInterface(binderPool?.queryBinder(BinderPool.BINDER_CODE_COMPUTE))
        }
        if (computeBinder != null) {
            val num1 = et_num1.text.toString().toDouble()
            val num2 = et_num2.text.toString().toDouble()
            val result = computeBinder?.getSum(num1, num2)
            tv_result.text = "$num1 + $num2 = $result"
        }
    }

    fun addBook(view: View) {
        if (bookManagerBinder == null) {
            bookManagerBinder = IBookManager.Stub.asInterface(binderPool?.queryBinder(BinderPool.BINDER_CODE_BOOK_MANAGER))
        }
        bookManagerBinder?.addBook(Book("好书", 66.6))
    }

    @SuppressLint("SetTextI18n")
    fun getBookList(view: View) {
        if (bookManagerBinder == null) {
            bookManagerBinder = IBookManager.Stub.asInterface(binderPool?.queryBinder(BinderPool.BINDER_CODE_BOOK_MANAGER))
        }
        tv_result.text = "bookList.size()=${bookManagerBinder?.bookList?.size}"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceConnection != null) {
            binderPool = null
            unbindService(serviceConnection)
        }
    }
}
