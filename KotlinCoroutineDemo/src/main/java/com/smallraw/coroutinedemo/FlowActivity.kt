package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

// Kotlin 协程操作流，与 JDK8 的 Streams API 使用类似
class FlowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow)
//        sequenceFun1()
//        sequenceFun2()
//        flowFun1()
//        flowFun2()
//        flowFun3()
//        flowFun4()
        flowFun5()
    }

    private fun fooSequence(): Sequence<Int> = sequence {
        // 序列构建器
        for (i in 1..3) {
            Thread.sleep(1000) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }

    private fun sequenceFun1() {
        fooSequence().forEach { value -> log("$value") }
    }

    suspend fun fooList(): List<Int> {
        delay(1000) // 假装我们在这里做了一些异步的事情
        return listOf(1, 2, 3)
    }

    private fun sequenceFun2() {
        GlobalScope.launch {
            fooList().forEach { value -> log("$value") }
        }
    }

    // 可以不用使用 suspend 标识了
    private fun foo() = flow {
        // 流构建器，可以挂起
        for (i in 1..3) {
            delay(1000) // 假装我们在这里做了一些有用的事情
            emit(i) // 发送下一个值
        }
    }

    // 使用 flow 发射流，flow 流与方法类似可以通过 collect 方法多次启动
    private fun flowFun1() = runBlocking {
        foo().collect { value -> log("$value") }
    }

    private fun flowFun2() = runBlocking {
        // flow 也是挂起函数，所以可以取消
        withTimeoutOrNull(230) {
            foo().collect { value -> log("$value") }
        }
    }

    // 可以使用 asFlow 来构建流
    private fun flowFun3() = runBlocking {
        // flow 也是挂起函数，所以可以取消
        (1..3).asFlow().collect { value -> log("$value") }
    }

    // 常用操作符示例
    private fun flowFun4() = runBlocking {
        // flow 也是挂起函数，所以可以取消
        (1..8)
            .asFlow()
            // 过滤操作符
            .filter {
                it > 5
            }
            // 过渡操作符
            .map {
                it
            }
            // 变换操作符
            .transform { value ->
                emit("$value")
                emit("${value + 1}")
            }
            // 数量限制操作符，只获取前三个
            .take(3)
            .collect { value -> log(value) }
    }

    // 结尾操作符,还有其他方法自测
    private fun flowFun5() = runBlocking {
        // 打印输出
        (1..8).asFlow()
            .collect { log("$it") }

        // 转换成列表
        (1..8).asFlow()
            .toList()

        // 转换成 Set
        (1..8).asFlow()
            .toSet()

        // 转换成字符串
        val toString = (1..8).asFlow()
            .toString()
        log("tostring:$toString")

        // 只获取第一个值
        val first = (1..8).asFlow()
            .first()
        log("first:$first")

        // 只有一个元素，否则报错
        val single = (1..8).asFlow()
            .take(1)
            .single()
        log("single:$single")

        // 求和 1 到 8
        val reduce = (1..8).asFlow()
            .reduce { a, b -> a + b }
        log("reduce:$reduce")

        // 求和 2 到 8，相比 reduce 可以指定从几开始
        val fold = (1..8).asFlow()
            .fold(1) { acc, i ->
                acc + i
            }
        log("fold:$fold")

    }
}
