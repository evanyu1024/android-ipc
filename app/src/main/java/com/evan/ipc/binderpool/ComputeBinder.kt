package com.evan.ipc.binderpool

class ComputeBinder : ICompute.Stub() {
    override fun getSum(num1: Double, num2: Double) = num1 + num2
}