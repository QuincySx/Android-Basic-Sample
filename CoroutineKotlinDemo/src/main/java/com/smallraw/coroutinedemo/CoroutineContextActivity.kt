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
        dispatchersFun1()
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
}
