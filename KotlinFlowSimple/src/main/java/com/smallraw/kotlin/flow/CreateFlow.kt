package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 * 创建流的方式
 */
fun main() {
    runBlocking {
        createFlowFun2().collect { println(it) }
    }

    println("Flow Time out")
    runBlocking {
        // Cancel flow
        try {
            withTimeout(3000) {
                createFlowFun1().collect { println(it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    println("Flow cancel")
    /**
     * .cancellable() 操作符配合 cancel() 即可取消
     */
    runBlocking {
        // Cancel flow
        createFlowFun2().cancellable().collect {
            if (it > 3) cancel()
            println(it)
        }
    }
}

/**
 * Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流执行 collect 的时候才开始运行。
 */
private fun createFlowFun1(): Flow<String> {
    var i = 0
    return flow {
        while (true) {
            emit("${i++}") // 将请求发送到 flow
            delay(1000) // Suspends the coroutine for some time
        }
    }
}

/**
 * Range、Array 或 Iterable 使用 asFlow() 转成 flow
 */
private fun createFlowFun2(): Flow<Int> {
    // return (1..3).asFlow()
    // return ArrayList<Int>().asFlow()
    return arrayListOf(1, 2, 3, 4, 5, 6).asFlow()
}

/**
 * flowOf() 转成 flow
 */
private fun createFlowFun3(): Flow<Int> {
    return flowOf(1, 2, 3, 4, 5)
}