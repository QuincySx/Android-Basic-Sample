package com.smallraw.kotlin.flow

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * 支持背压的操作符
 */
fun main() {
//    flowBuffer()
//    flowConflate()
    flowCollectLatest()
}

/**
 * 累计求和等与上下文关联的操作
 */
private fun flowBuffer() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .onEach { delay(100) }
            /**
             * 流的缓存策略
             * capacity
             *      默认 BUFFERED 一个具有默认缓冲容量的缓冲通道。冷流默认容量是 64，可以通过在 JVM 上设置 DEFAULT_BUFFER_PROPERTY_NAME 来覆盖。对于热流容量为 1。
             *      CONFLATED 它创建一个最多缓冲一个元素的通道，只有最后发送的元素被接收，而之前发送的元素会丢失。向这个通道的发送调用方永远不会挂起，而且 trySend 总是成功的。
             *      UNLIMITED 可以无限量缓存的通道，如果太多可能会 OOM。
             *      RENDEZVOUS（0）创建一个没有缓存的通道，如果没有接受者，发送调用方会挂起。直到有接受者为止。
             * onBufferOverflow 可以设置相关策略来处理缓冲区中已存满要发送的数据项的情况。默认值为 BufferOverflow.SUSPEND，这会使调用方挂起。当 capacity >= 0 或 capacity == Channel.BUFFERED 才会生效。
             */
            .buffer()
            .collect {
                delay(300)
                println(it)
            }
    }
}

/**
 * 略过中间的值，只处理最新的数据
 * 可以接受多个
 */
private fun flowConflate() {
    runBlocking {
        flowOf("1", "2", "3", "4", "5")
            .onEach { delay(100) }
            /**
             * 对应 LATEST 策略,如果缓存池满了，新数据会覆盖老数据
             */
            .conflate()
            .collect {
                delay(300)
                println(it)
            }
    }
}

/**
 * 略过中间的值，只处理最新的数据
 * 可以接受多个
 */
private fun flowCollectLatest() {
    runBlocking {
        println("collectLatest 挂起实验")
        flowOf("1", "2", "3", "4", "5")
            .onEach { delay(100) }
            /**
             * 它并不会直接用新数据覆盖老数据，而是每一个都会被处理，只不过如果前一个还没被处理完后一个就来了的话，处理前一个数据的逻辑就会被取消。
             * 业务流程他会处理到一半
             */
            .collectLatest { value ->
                println("Collecting $value")
                delay(90)
                println("Collecting delay1 $value")
                delay(90)
                println("Collecting delay2 $value")
                delay(90)
                println("$value collected")
            }

        println("collectLatest 线程耗时实验")
        flowOf("1", "2", "3", "4", "5")
            .onEach { delay(100) }
            /**
             * 如果中间没有 挂起 函数的话，他也不会被取消，也会老老实实的每个都执行完。
             */
            .collectLatest { value ->
                println("Collecting $value")
                Thread.sleep(90)
                println("Collecting delay1 $value")
                Thread.sleep(90)
                println("Collecting delay2 $value")
                Thread.sleep(90)
                println("$value collected")
            }
    }
}