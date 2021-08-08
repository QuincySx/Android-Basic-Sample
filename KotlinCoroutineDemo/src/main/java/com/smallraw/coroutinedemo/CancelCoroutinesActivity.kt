package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import java.lang.Exception

class CancelCoroutinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancel_coroutines)
//        cancelFun1()
//        cancelFun2()
//        cancelFun3()
//        cancelFun4()
        cancelFun5()
    }

    // 挂起函数是可以实时取消的
    private fun cancelFun1() {
        GlobalScope.launch {
            val job = launch {
                repeat(100) { i ->
                    log("cancelFun1 工作 $i ...")
                    delay(500L)
                }
            }
            delay(1300L) // 延迟一段时间
            log("cancelFun1 准备取消任务")
            job.cancel() // 取消该作业
            job.join() // 等待作业执行结束
            // job.cancelAndJoin() 取消并等待，简写。
            log("cancelFun1 任务已经结束")
        }
    }

    // 自己写的耗时方法是无法真正停止的，下面的例子循环会执行完毕。解决办法看 例3
    private fun cancelFun2() {
        GlobalScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                while (i < 10) { // 一个执行计算的循环，只是为了占用 CPU
                    // 每秒打印消息两次
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("cancelFun2 工作 ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1300L) // 等待一段时间
            log("cancelFun2 准备取消任务")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            log("cancelFun2 任务已经结束")
        }
    }

    // 这与线程停不下来的问题一致，不过协程为我们提供了 isActive 属性。
    private fun cancelFun3() {
        GlobalScope.launch {
            val startTime = System.currentTimeMillis()
            val job = launch {
                var nextPrintTime = startTime
                var i = 0
                // isActive 属性是 CoroutineScope 中提供的，表示当前任务的运行状态。
                while (i < 10 && isActive) { // 一个执行计算的循环，只是为了占用 CPU
                    if (System.currentTimeMillis() >= nextPrintTime) {
                        log("cancelFun3 工作 ${i++} ...")
                        nextPrintTime += 500L
                    }
                }
            }
            delay(1600L) // 等待一段时间
            log("cancelFun3 准备取消任务")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            log("cancelFun3 任务已经结束")
        }
    }

    // 停止协程前做一些资源释放，不允许跳过。可以使用 withContext(NonCancellable) 挂起函数。
    private fun cancelFun4() {
        GlobalScope.launch {
            val job = launch {
                try {
                    repeat(1000) { i ->
                        log("cancelFun4 工作 $i ...")
                        delay(500L)
                    }
                } finally {
                    // 使用挂起函数 并标记不可取消
                    withContext(NonCancellable) {
                        log("cancelFun4 资源释放")
                        delay(1000L)
                        log("cancelFun4 资源释放完毕")
                    }
                }
            }
            delay(1600L) // 等待一段时间
            log("cancelFun4 准备取消任务")
            job.cancelAndJoin() // 取消一个作业并且等待它结束
            log("cancelFun4 任务已经结束")
        }
    }

    // 协程挂起超时取消
    private fun cancelFun5() {
        GlobalScope.launch {
            try {
                val result = withTimeout(1300) {
                    repeat(1000) { i ->
                        log("cancelFun5 withTimeout 工作 $i ...")
                        delay(500L)
                    }
                    "返回值"
                }
                log("cancelFun5 withTimeout 任务已经结束 result:$result")
            } catch (e: Exception) {
                // 抛出 TimeoutCancellationException 异常
                e.printStackTrace()
            }

            // withTimeoutOrNull 不会报错，但是返回结果是空的
            val result = withTimeoutOrNull(1300) {
                repeat(1000) { i ->
                    log("cancelFun5 withTimeoutOrNull 工作 $i ...")
                    delay(500L)
                }
                "返回值"
            }
            log("cancelFun5 withTimeoutOrNull 任务已经结束 result:$result")
        }
    }
}
