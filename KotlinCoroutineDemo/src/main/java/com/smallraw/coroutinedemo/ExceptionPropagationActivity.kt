package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import java.lang.Exception

private fun coroutineExceptionHandler() = CoroutineExceptionHandler { _, exception ->
    exception.printStackTrace()
}

class ExceptionPropagationActivity : AppCompatActivity(),
    CoroutineScope by MainScope() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exception_propagation)
        coroutineContext + coroutineExceptionHandler()
//        fun1()
        fun2()
    }

    private suspend fun errorFunction() {
        delay(1000)
        throw RuntimeException("我是错误")
    }

    private fun fun1() {
        launch(Dispatchers.IO + coroutineExceptionHandler()) {
            errorFunction()
        }
    }

    private fun fun2() {

        launch(Dispatchers.IO + coroutineExceptionHandler()) {
            val async = async(SupervisorJob()) {
                errorFunction()
            }
            async.await()
        }


    }
}