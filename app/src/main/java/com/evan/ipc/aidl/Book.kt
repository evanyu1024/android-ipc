package com.evan.ipc.aidl

import android.os.Parcel
import android.os.Parcelable

/**
 * @author evanyu
 * @date 2018/9/27
 */
class Book(var name: String, var price: Double) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Book> {
        override fun createFromParcel(parcel: Parcel): Book {
            return Book(parcel)
        }

        override fun newArray(size: Int): Array<Book?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "Book(name='$name', price=$price)"
    }
}