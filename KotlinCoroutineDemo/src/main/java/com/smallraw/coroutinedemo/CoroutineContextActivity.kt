package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.RejectedExecutionException
import kotlin.coroutines.CoroutineContext

class CoroutineContextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_context)
//        dispatchersFun1()
//        dispatchersFun2()
//        dispatchersFun3()
//        dispatchersFun4()
//        coroutineContextFun1()
        coroutineContextFun2()
    }

    // 自定义 Dispatcher
    private val customDispatcher = object : ExecutorCoroutineDispatcher() {
        override val executor: Executor
            get() = Executors.newSingleThreadExecutor {
                Thread(it, "我是个线程")
            }

        override fun close() {
            (executor as ExecutorService).shutdown()
        }

        override fun dispatch(context: CoroutineContext, block: Runnable) {
            try {
                executor.execute(block)
            } catch (e: RejectedExecutionException) {
                e.printStackTrace()
            }
        }
    }

    private fun dispatchersFun1() {
        runBlocking {
            launch {
                // 运行在父协程的上下文中，此代码 runBlocking 是父协程
                log("main runBlocking      : 工作线程")
            }
            launch(Dispatchers.Unconfined) {
                // 不受限的——将工作在主线程中
                log("Unconfined            : 工作线程")
            }
            launch(Dispatchers.Default) {
                // 将会获取默认调度器，这是 launch 的默认参数。
                log("Default               : 工作线程")
            }
            // 以后 Kotlin 可能会改，生产环境不建议使用。
            launch(newSingleThreadContext("MyOwnThread")) {
                // 将使它获得一个新的线程
                log("newSingleThreadContext: 工作线程")
            }
            launch(customDispatcher) {
                // 自定义线程调度器
                log("customDispatcher      : 工作线程")
            }
        }
    }

    // 使用 launch(Dispatchers.Unconfined) 的 CoroutineScope 执行挂起函数完毕，该挂起函数后面的代码线程执行环境会变成该挂起函数所使用的线程。
    private fun dispatchersFun2() {
        runBlocking {
            launch(Dispatchers.Unconfined) {
                // 非受限的——将和主线程一起工作
                log("Unconfined      : 第一个挂起任务 开始")
                dispatchersFun2Suspend1()
                log("Unconfined      : 第一个挂起任务 结束")

                log("Unconfined      : 第二个挂起任务 开始")
                dispatchersFun2Suspend2()
                log("Unconfined      : 第二个挂起任务 结束")
            }
            launch {
                // 父协程的上下文，主 runBlocking 协程
                log("main runBlocking: 工作线程")
                delay(1000)
                log("main runBlocking: 工作完成")
            }
        }
    }

    private suspend fun dispatchersFun2Suspend1() {
        withContext(newSingleThreadContext("线程一")) {
            log("Unconfined      : 工作中..")
            delay(500)
        }
    }

    private suspend fun dispatchersFun2Suspend2() {
        withContext(newSingleThreadContext("线程二")) {
            log("Unconfined      : 工作中..")
            delay(500)
        }
    }

    // 主协程取消时子协程也会被关闭
    private fun dispatchersFun3() {
        GlobalScope.launch {
            // 启动一个协程来处理某种传入请求（request）
            val request = launch {
                // 孵化了两个子作业, 其中一个通过 GlobalScope 启动
                GlobalScope.launch {
                    log("子任务1 开始")
                    delay(1000)
                    log("子任务1 结束")
                }
                // 另一个则承袭了父协程的上下文
                launch {
                    delay(100)
                    log("子任务2 开始")
                    delay(1000)
                    log("子任务2 结束")
                }
            }
            delay(500)
            request.cancel() // 取消请求（request）的执行
            delay(1000) // 延迟一秒钟来看看发生了什么
            log("主协程 结束")
        }
    }

    // 主协程会一直等待子协程执行完毕
    private fun dispatchersFun4() {
        GlobalScope.launch {
            val launch = GlobalScope.launch {
                launch {
                    log("子任务 开始")
                    delay(1000)
                    log("子任务 结束")
                }
                log("子任务要开始执行了")
            }
            // 等待主协程时，他也会等待子协程全部执行完毕
            launch.join()
            log("主协程 结束")
        }
    }

    // 可以使用 + 操作符连接多个 协程上下文
    private fun coroutineContextFun1() {
        GlobalScope.launch {
            launch(Dispatchers.Default + CoroutineName("test")) {
                log("工作线程")
            }
        }
    }

    private fun coroutineContextFun2() {
        // 可以指定协程名称 CoroutineName
        GlobalScope.launch(CoroutineName("可爱的协程")) {
            // isActive 就是 coroutineContext[Job]?.isActive ?: true
            log("${coroutineContext[Job]}")
        }
    }
}
