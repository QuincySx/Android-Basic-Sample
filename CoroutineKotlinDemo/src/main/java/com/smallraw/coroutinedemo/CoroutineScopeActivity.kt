package com.smallraw.coroutinedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

// 简写 1
class TestClass1 : CoroutineScope by MainScope() {
    // doSomething
}

// 简写 2
class TestClass2 {
    val mainScope = MainScope()

    fun doSomething() {
        mainScope.launch {
            // doSomething
        }
    }

    fun destroy() {
        mainScope.cancel()
    }
}

// 给 CoroutineScopeActivity 这个类
class CoroutineScopeActivity : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = MainScope().coroutineContext

    fun doSomething() {
        launch {
            // doSomething
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineContext.cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_scope)
    }
}
