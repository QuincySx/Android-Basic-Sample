package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() {
//    sharedFlowNormal()
//    sharedShareIn()
    sharedShareInWhileSubscribed()
}

/**
 * SharedFlow 是一种类似于序列的热流 — 这段 flow 构建器中的代码开始的时候才开始运行。而不是等到 collect 再运行。
 */
private fun sharedFlowNormal() {
    /**
     * 通过 replay 设置针对新订阅者重新发送之前已发出的数据项数目。
     * 通过 extraBufferCapacity 设置除“replay”外缓冲的数据项数目。
     * 通过 onBufferOverflow 可以设置相关策略来处理缓冲区中已存满要发送的数据项的情况。默认值为 BufferOverflow.SUSPEND，这会使调用方挂起。其他选项包括 DROP_LATEST 或 DROP_OLDEST。
     */
    val flow = MutableSharedFlow<Int>(replay = 0)

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


private fun sharedShareIn() {
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
    val flow = flowOf(1, 2, 3, 4, 5, 6, 7).onEach { delay(100) }
        .shareIn(CoroutineScope(Dispatchers.IO), SharingStarted.Eagerly)

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

private fun sharedShareInWhileSubscribed() {
    /**
     * SharingStarted.WhileSubscribed() 可以自定义等多长时间没人订阅自动取消。
     */
    val flow by lazy {
        flowOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).onEach { delay(100) }
            .shareIn(
                CoroutineScope(Dispatchers.IO),
                SharingStarted.WhileSubscribed(500)
            )
    }

    runBlocking {
        try {
            delay(600)
            withTimeout(500) {
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


    runBlocking {
        delay(1000)
        /**
         * 每次重新订阅都会重新执行
         */
        flow
            .onSubscription { println("onSubscription") }
            .onStart { println("onStart") }
            .onCompletion { println("onCompletion") }
            .collect {
                println(it)
            }
    }
}