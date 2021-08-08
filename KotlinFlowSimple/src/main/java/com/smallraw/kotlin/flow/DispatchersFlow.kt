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
            // 只能切换到上一个线程环境，有些像 Rxjava.subscribeOn()
            .flowOn(Dispatchers.IO)
            .map {
                println("map2：Current Thread: ${Thread.currentThread().name}")
                it
            }
            // 可以切换多次
            .flowOn(newSingleThreadContext("MyOwnThread"))
            .collect {
                // 线程跟随外部 Scope 的线程
                println("collect：Current Thread: ${Thread.currentThread().name}")
                println(it)
            }
    }
}