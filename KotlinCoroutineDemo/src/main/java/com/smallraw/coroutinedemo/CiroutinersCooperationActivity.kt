package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*

class CiroutinersCooperationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ciroutiners_cooperation)
        cooperationFun1()
        cooperationFun2()
        cooperationFun3()
        cooperationFun4()
    }

    // 延时阻塞挂起
    private fun cooperationFun1() {
        GlobalScope.launch {
            log("test1 begin")
            // 挂起，假设是任务执行
            delay(1000)
            log("test1 end")
        }
    }

    // 等待任务完成
    private fun cooperationFun2() {
        GlobalScope.launch {
            // 下面代码可以使用 GlobalScope.launch 另创建一个顶层协程，但是会消耗一下资源。
            // 所以在 CoroutineScope 中可直接使用 launch 启动携程，以节省资源。
            val launch = launch {
                log("test2 begin")
                delay(1000)
            }
            launch.join()
            log("test2 end")
        }
    }

    // 惰性有返回值的等待任务完成
    private fun cooperationFun3() {
        GlobalScope.launch {
            log("cooperationFun3 start")
            val one = async { doSomethingUsefulOne() }
            val two = async { doSomethingUsefulTwo() }
            log("cooperationFun3 end result:${one.await() + two.await()}")
        }
    }

    // 惰性有返回值的等待任务完成
    private fun cooperationFun4() {
        GlobalScope.launch {
            log("cooperationFun4 start")
            val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
            val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
            // 执行一些计算
            one.start() // 启动第一个
            two.start() // 启动第二个
            log("cooperationFun4 end result:${one.await() + two.await()}")
        }
    }

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // 假设我们在这里做了一些有用的事
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1500L) // 假设我们在这里也做了一些有用的事
        return 29
    }
}
