package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() {
//    stateFlowNormal()
//    stateShareIn()
    stateShareInWhileSubscribed()
}

/**
 * SharedFlow 是 SharedFlow 热流的一种实现。他的默认缓存容量大小是 1。
 */
private fun stateFlowNormal() {
    /**
     * 只有默认值。
     */
    val flow = MutableStateFlow<Int>(0)

    CoroutineScope(Dispatchers.IO).launch {
        /**
         * emit 方法：当缓存策略为 BufferOverflow.SUSPEND 时，emit 方法会挂起，直到有新的缓存空间。
         * tryEmit 方法：tryEmit 会返回一个 Boolean 值，true 代表传递成功，false 代表会产生一个回调，让这次数据发射挂起，直到有新的缓存空间。
         */
        flow.emit(1)
        delay(100)
        flow.emit(2)
        delay(200)
        flow.emit(3)
        delay(300)
        flow.emit(4)
        delay(1000)
        flow.emit(5)
        delay(2000)
        flow.emit(6)
    }

    CoroutineScope(Dispatchers.Default).launch {
        delay(400)
        flow
            .onSubscription { println("onSubscription") }
            .onStart { println("onStart") }
            .onCompletion { println("onCompletion") }
            .collect {
                println(it)
            }
    }

    Thread.sleep(5 * 1000)
}


private fun stateShareIn() {
    /**
     * 通过 scope 设置热流创建的线程环境。
     * 通过 started 可以设置发送的状况。 SharingStarted.Eagerly 立马就开始运行热流，SharingStarted.Lazily 等有订阅者消费才会运行热流，SharingStarted.WhileSubscribed() 自定义。
     * 通过 replay 设置针对新订阅者重新发送之前已发出的数据项数目。
     */
    /**
     * SharingStarted.WhileSubscribed( 可以自定义等多长时间没人订阅自动暂停热流。
     *    stopTimeoutMillis 最后一个订阅者消失时，多长时间后热流将停止。默认是 0。
     *    replayExpirationMillis 当最后一个订阅者消失，缓存存在多少时间。默认是 永远不。
     * )
     */
    runBlocking {
        val flow = withContext(Dispatchers.IO) {
            flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).onEach { delay(100) }
                .stateIn(CoroutineScope(Dispatchers.IO))
        }

        withContext(Dispatchers.Default) {
            delay(300)
            try {
                withTimeout(200) {
                    flow
                        .onSubscription { println("onSubscription") }
                        .onStart { println("onStart") }
                        .onCompletion { println("onCompletion") }
                        .collect {
                            println(it)
                        }
                }
            } catch (e: Exception) {
            }
        }

        delay(300)
        flow
            .onSubscription { println("onSubscription") }
            .onStart { println("onStart") }
            .onCompletion { println("onCompletion") }
            .collect {
                println(it)
            }
    }
}

private fun stateShareInWhileSubscribed() {
    /**
     * 通过 scope 设置热流创建的线程环境。
     * 通过 started 可以设置发送的状况。 SharingStarted.Eagerly 立马就开始运行热流，SharingStarted.Lazily 等有订阅者消费才会运行热流，SharingStarted.WhileSubscribed() 自定义。
     * 通过 replay 设置针对新订阅者重新发送之前已发出的数据项数目。
     */
    /**
     * SharingStarted.WhileSubscribed( 可以自定义等多长时间没人订阅自动暂停热流。
     *    stopTimeoutMillis 最后一个订阅者消失时，多长时间后热流将停止。默认是 0。
     *    replayExpirationMillis 当最后一个订阅者消失，缓存存在多少时间。默认是 永远不。
     * )
     */
    runBlocking {
        val flow = withContext(Dispatchers.IO) {
            flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).onEach { delay(100) }
                .shareIn(
                    CoroutineScope(Dispatchers.IO),
                    SharingStarted.WhileSubscribed(500)
                )
                .stateIn(CoroutineScope(Dispatchers.IO))
        }

        withContext(Dispatchers.Default) {
            delay(300)
            try {
                withTimeout(200) {
                    flow
                        .onSubscription { println("onSubscription") }
                        .onStart { println("onStart") }
                        .onCompletion { println("onCompletion") }
                        .collect {
                            println(it)
                        }
                }
            } catch (e: Exception) {
            }
        }

        withContext(Dispatchers.Default) {
            delay(300)
            try {
                withTimeout(200) {
                    flow
                        .onSubscription { println("onSubscription") }
                        .onStart { println("onStart") }
                        .onCompletion { println("onCompletion") }
                        .collect {
                            println(it)
                        }
                }
            } catch (e: Exception) {
            }
        }

        delay(500)
        flow
            .onSubscription { println("onSubscription") }
            .onStart { println("onStart") }
            .onCompletion { println("onCompletion") }
            .collect {
                println(it)
            }
    }
}