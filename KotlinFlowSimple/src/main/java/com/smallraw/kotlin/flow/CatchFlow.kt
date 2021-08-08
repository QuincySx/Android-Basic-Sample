package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun main() {
//    catch1()
//    catch2()
//    catch3()
//    lifecycle()
    flowEmpty()
}

private fun lifecycle() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .onEach {
                delay(100)
            }
            .map {
                if (it > 3) throw RuntimeException("error")
                it
            }
            .catch {
                println("error: ${it.message}")
                // 在 catch 中发送数据
                emit(-1)
            }
            .onStart { println("flow Start.") }
            .onCompletion { println("flow Done.")}
            .onEmpty { println("flow Empty.") }
            .collect { println(it) }
    }
}

private fun flowEmpty() {
    runBlocking {
        emptyFlow<Int>()
            .onEmpty { println("flow Empty.") }
            .onStart { println("flow Start.") }
            .onCompletion { println("flow Done.")}
            .collect { println(it) }
    }
}

private fun catch1() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .onEach {
                delay(100)
            }
            .map {
                // 结果为 true 不报错
                check(it < 3) { "error" }
                it
            }
            .catch { println("error: ${it.message}") }
            .collect { println(it) }
    }
}

private fun catch2() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .onEach {
                delay(100)
            }
            .map {
                if (it > 3) throw RuntimeException("error")
                it
            }
            .catch { println("error: ${it.message}") }
            .collect { println(it) }
    }
}

private fun catch3() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .onEach {
                delay(100)
            }
            .map {
                if (it > 3) throw RuntimeException("error")
                it
            }
            .catch {
                println("error: ${it.message}")
                // 在 catch 中发送数据
                emit(-1)
            }
            .collect { println(it) }
    }
}