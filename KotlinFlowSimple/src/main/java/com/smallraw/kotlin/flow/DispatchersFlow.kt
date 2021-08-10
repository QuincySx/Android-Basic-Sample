package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking

fun main() {
    flowOn()
}

private fun flowOn() {
    runBlocking(Dispatchers.Default) {
        flowOf(1, 2, 3, 4, 5)
            .map {
                println("map1：Current Thread: ${Thread.currentThread().name}")
                it
            }
            // 只能切换从此处到流的发送源或上一个 flowOn 的线程环境，有些像 Rxjava.subscribeOn()。
            .flowOn(Dispatchers.IO)
            .map {
                println("map2：Current Thread: ${Thread.currentThread().name}")
                it
            }
            // flowOn 可以切换多次并不想 Rxjava.subscribeOn() 那样只能运行一次。
            .flowOn(newSingleThreadContext("MyOwnThread"))
            .collect {
                // 线程跟随外部 Scope 的线程
                println("collect：Current Thread: ${Thread.currentThread().name}")
                println(it)
            }
    }
}