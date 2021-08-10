package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

/**
 * 变换操作符
 */
fun main() {
//    flowMap()
//    flowFilter()
//    flowFlatMapConcat()
//    flowTransform()
//    flowTake()
//    flowDrop()
//    flowDebounce()
//    flowRetry()
//    flowOnEach()
//    flowCombine()
//    flowZip()
//    flowDistinctUntilChanged()
//    flowScan()
    flowFlattenMerge()
}

/**
 * 变换
 */
private fun flowMap() {
    runBlocking {
        createFlowFun1()
            .map {
                "print:$it"
            }
            .collect { println(it) }

        createFlowFun1()
            // 只操作最后一个
            .mapLatest {
                "print:$it"
            }
            .collect { println(it) }
    }
}

/**
 * 过滤
 */
private fun flowFilter() {
    runBlocking {
        createFlowFun1()
            .filter {
                it != "2"
            }
            .collect { println(it) }
    }
}

/**
 * 重新变换发送者
 */
private fun flowFlatMapConcat() {
    runBlocking {
        flowOf("12", "23", "34")
            .flatMapConcat {
                flow {
                    it.forEach { char ->
                        emit(char)
                    }
                }
            }
            .collect { println(it) }

        flowOf("12", "23", "34")
            // 只转换最后一个
            .flatMapLatest {
                flow {
                    it.forEach { char ->
                        emit(char)
                    }
                }
            }
            .collect { println(it) }
    }
}

/**
 * 变换
 */
private fun flowTransform() {
    runBlocking {
        flowOf("12", "23", "34")
            .transform { request ->
                emit(request)
            }
            .collect { println(it) }
    }
}

/**
 * 限长操作符
 */
private fun flowTake() {
    runBlocking {
        flowOf("1", "2", "3", "4", "5")
            .take(2) // 只取前两个
            .collect { println(it) }
    }
}

/**
 * 跳过操作符
 */
private fun flowDrop() {
    runBlocking {
        flowOf("1", "2", "3", "4", "5")
            .drop(2) // 跳过前两个
            .collect { println(it) }
    }
}


/**
 * 防止重复，一定时间内只能发一次
 */
private fun flowDebounce() {
    runBlocking {
        flow {
            emit(1)
            delay(90)
            emit(2)
            delay(900)
            emit(3)
            delay(1090)
            emit(4)
            delay(1010)
            emit(5)
        }
            .debounce { 1000 }
            .collect { println(it) }
    }
}

/**
 * 去重操作符
 */
private fun flowDistinctUntilChanged() {
    runBlocking {
        println("distinctUntilChanged")
        flowOf("1", "1", "2", "2", "3")
            .distinctUntilChanged()
            .collect {
                println(it)
            }

        println("distinctUntilChangedBy")
        flowOf("11", "12", "13", "23", "24")
            .distinctUntilChangedBy { it.first() }
            .collect {
                println(it)
            }
    }
}


/**
 * 合并操作符：压缩两个流
 * 1 ---2 ---3
 * A ------B ------C
 * 1A--2A--3A\3B---3C
 */
private fun flowCombine() {
    runBlocking {
        val numbersFlow = flowOf(1, 2, 3).onEach { delay(1000) }
        val lettersFlow = flowOf("A", "B", "C").onEach { delay(2000) }

        numbersFlow.combine(lettersFlow) { number, letter ->
            "$number$letter"
        }.collect {
            println(it)
        }
    }
}

/**
 * 合并操作符：压缩两个流
 * 1 ---2 ---3
 * A ------B ------C ------D
 * 1A------2B------3C
 */
private fun flowZip() {
    runBlocking {
        val numbersFlow = flowOf(1, 2, 3).onEach { delay(1000) }
        val lettersFlow = flowOf("A", "B", "C", "D").onEach { delay(2000) }

        numbersFlow.distinctUntilChangedBy { }

        numbersFlow.zip(lettersFlow) { number, letter ->
            "$number$letter"
        }.collect {
            println(it)
        }
    }
}

/**
 * 合并多个 flow，（展平流）
 */
private fun flowFlattenMerge() {
    runBlocking {
        val flows = flowOf(flowOf(1, 2, 3), flowOf("A", "B", "C"))
        flows.flattenMerge()
            .conflate()
            .collect {
                println(it)
            }
    }
}

/**
 * 循环、延时发送
 */
private fun flowOnEach() {
    runBlocking {
        flowOf("1", "2", "3", "4", "5")
            .onEach {
                delay(1000)
            }
            .take(2)
            .collect { println(it) }
    }
}

/**
 * 重试操作符
 */
private fun flowRetry() {
    runBlocking {
        flow {
            val nextInt = Random.nextInt(10)
            if (nextInt < 3) {
                throw IllegalArgumentException()
            }
            if (nextInt < 7) {
                throw RuntimeException()
            }
            emit(nextInt)
        }
            .retry(3) { cause ->
                println("是否需要重试？")
                if (cause is RuntimeException) {
                    println("重试")
                    return@retry true
                }
                println("不再重试")
                return@retry false
            }
            .catch {
                println("3 次重试失败")
            }
            .collect { println(it) }
    }
}

/**
 * 累计求和等与上下文关联的操作
 */
private fun flowScan() {
    runBlocking {
        flowOf(1, 2, 3, 4, 5)
            .scan(1) { accumulator, value ->
                return@scan accumulator + value
            }
            .collect { println(it) }
    }
}

/**
 * Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流执行 collect 的时候才开始运行。
 */
private fun createFlowFun1(): Flow<String> {
    var i = 0
    return flow {
        while (i < 10) {
            emit("${i++}") // 将请求发送到 flow
            delay(1000) // Suspends the coroutine for some time
        }
    }
}
