package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import kotlin.math.log

class ToggleCoroutinesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toggle_coroutines)
        toggleFun1()
        toggleFun2()
        toggleFun3()
        toggleFun4()
        toggleFun5()
        toggleFun6()
        toggleFun7()
        toggleFun8()
    }

    private fun toggleFun1() {
        GlobalScope.launch {
            log("1切换前")
            launch(Dispatchers.Main) {
                log("1切换后")
            }
        }
    }

    private fun toggleFun2() {
        GlobalScope.launch {
            log("2切换前")
            withContext(Dispatchers.Main) {
                log("2切换后")
            }
        }
    }

    private fun toggleFun3() {
        GlobalScope.launch {
            log("3切换前")
            GlobalScope.launch {
                log("3切换后")
            }
        }
    }

    private fun toggleFun4() {
        GlobalScope.launch {
            val avatar = async {
                delay(1000)
                "打印返回值"
            }
            log("==准备==")
            log(avatar.await())
        }
    }

    private fun toggleFun5() {
        GlobalScope.launch {
            // 切换协程方式 1
            launch {
                log("test5 launch")
            }
            // 切换协程方式 2
            coroutineScope {
                log("test5 coroutineScope begin")
                delay(500)
                log("test5 coroutineScope end")
            }
            // 切换协程方式 3 , 不推荐的写法，runBlocking 应改为顶层协程开启函数
            runBlocking {
                log("test5 runBlocking begin")
                delay(500)
                log("test5 runBlocking end")
            }
            log("test5 end")
        }
        // 以上代码 runBlocking 与 coroutineScope 运行结果和功能相似

        // 官方对比了 coroutineScope 和 runBlocking 个人觉得是有歧义的。
        // 如下代码 coroutineScope 后面的 end 先执行，然后执行 coroutineScope 的 after。
        // runBlocking 先执行 after 在执行 end。个人觉得没有什么和对比性, dis 官方文档。
        GlobalScope.launch {
            coroutineScope {
                log("test6 coroutineScope begin")
                delay(1500)
                log("test6 coroutineScope after ")
            }
        }
        log("test6 coroutineScope end")

        runBlocking {
            log("test6 runBlocking begin")
            delay(1500)
            log("test6 runBlocking after ")
        }
        log("test6 runBlocking end")
    }

    private fun toggleFun6() {
        GlobalScope.launch {
            // 可以使用
            log("suspend 方法之前")
            runFunction()
            log("suspend 方法之后")
        }
    }

    // 挂起函数的目的是为了标记该方法内可以做挂起操作(调用挂起函数)，任务执行完毕后协程可以切回到该方法继续执行。
    // 真正的挂起操作是在切协程的时候做的。
    // 除此之外挂起函数与普通方法一样。
    private suspend fun runFunction() {
        // 方法里可以放所有挂起函数，（suspend 关键字修饰的函数）
        delay(1000)
        log("执行 suspend 方法体")
        coroutineScope {}
        // 调用 withContext 挂起函数，挂起切线程。
        withContext(Dispatchers.IO) {
            delay(1000)
            log("切线程后继续执行 suspend 方法体")
        }
    }

    // async 异步方法
    // 与 launch 的异同。
    // async  有返回值     有 join 方法
    // launch 没有返回值   有 join 方法
    private fun toggleFun7() {
        GlobalScope.launch {
            // 可以使用
            log("async 方法之前")
            val async = async { asyncFunction() }
            // val job = launch { asyncFunction() }
            // job.join()
            log("async 方法之后")
            log("等待 async ${async.await()}")
        }
    }

    private suspend fun asyncFunction(): String {
        delay(1000)
        return "我是返回值"
    }

    // 启动很多个协程,类似线程池执行多个任务
    private fun toggleFun8() {
        GlobalScope.launch {
            repeat(10) { i ->
                log("toggleFun8 I'm sleeping $i ...")
                delay(500L)
            }
        }
    }
}
