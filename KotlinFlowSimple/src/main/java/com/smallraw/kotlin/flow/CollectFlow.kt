package com.smallraw.androidbasicsample.livedata

import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

/**
 * 消费流的方式
 */
fun main() {
    collectNormal()
}

private fun collectNormal() {
    runBlocking {
        println("collect: collect")
        createFlowFun1()
            .collect { println(it) }


        println("collect: collectIndexed")
        createFlowFun1()
            .collectIndexed { index, value ->
                println("index:$index   value:$value")
            }


        println("collect: count")
        val count = createFlowFun1()
            .count()
        println(count)


        println("collect: reduce")
        /**
         * 求和或其他操作
         */
        val reduce = createFlowFun1()
            .reduce { accumulator, value ->
                println("reduce: accumulator:$accumulator   value:$value")
                return@reduce accumulator * value
            }
        println(reduce)


        println("collect: fold")
        /**
         * fold(初始值) 与 reduce 类似
         */
        val fold = createFlowFun1()
            .fold(0) { acc, value ->
                println("fold: acc:$acc   value:$value")
                return@fold acc + value
            }
        println(fold)


        createFlowFun1().first()
        createFlowFun1().last()
        createFlowFun1().toList()
        createFlowFun1().toSet()
        println(createFlowFun1().take(1).single())
    }
}

/**
 * Flow 是一种类似于序列的冷流 — 这段 flow 构建器中的代码直到流执行 collect 的时候才开始运行。
 */
private fun createFlowFun1(): Flow<Int> {
    return flowOf(1, 2, 3, 4, 5, 6)
}
