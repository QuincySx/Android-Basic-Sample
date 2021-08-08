package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StartCoroutinesActivity : AppCompatActivity() {
    // 协程是依赖线程运行的，有点像高级版的线程池。他的利用率和任务切换效率高于线程切换。
    // Kotlin 协程由 CoroutineScope 和 CoroutineContext 组成。
    // CoroutineScope 范围内使用 launch(CoroutineContext) 启动新的协程。
    // CoroutineContext 是协程的运行线程环境。
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_coroutines)
        fun1()
        fun2()
        fun3()
        startCoroutineContext()
    }

    private fun fun1() {
        //方法一，使用 runBlocking 顶层函数，不传入 context 就是默认线程
        runBlocking {
            log("==启动协程2==")
        }
    }

    private fun fun2() {
        //方法二，使用 GlobalScope 单例对象
        GlobalScope.launch {
            log("==启动协程1==")
        }
    }

    private fun fun3() {
        //方法三，自行通过 CoroutineContext 创建一个 CoroutineScope 对象
        val coroutineContext = newFixedThreadPoolContext(2, "custom")

        // Android 协程包里自带的 CoroutineContext
        //val coroutineContext = Dispatchers.Main
        CoroutineScope(coroutineContext).launch {
            log("==启动协程3==")
        }
    }

    private fun startCoroutineContext(){
        // 可以通过指定 CoroutineContext 来切换协程运行的线程。
        runBlocking(Dispatchers.IO) {
            log("==启动切换协程1==")
        }
        GlobalScope.launch(Dispatchers.IO) {
            log("==启动切换协程2==")
        }
    }
}
