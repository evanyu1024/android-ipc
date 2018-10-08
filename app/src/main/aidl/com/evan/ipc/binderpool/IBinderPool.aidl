// IBinderPool.aidl
package com.evan.ipc.binderpool;

interface IBinderPool {

    IBinder queryBinder(int binderCode);

}
