package com.evan.ipc.aidl;

import com.evan.ipc.aidl.Book;
import com.evan.ipc.aidl.OnBookListChangedListener;

/**
 * 自定义 AIDL 接口
 * PS：AIDL 接口中只支持方法，不支持静态常量
 */
interface IBookManager {

    void addBook(in Book book);

    List<Book> getBookList();

    void addOnBookListChangedListener(OnBookListChangedListener listener);

    void removeOnBookListChangedListener(OnBookListChangedListener listener);
}