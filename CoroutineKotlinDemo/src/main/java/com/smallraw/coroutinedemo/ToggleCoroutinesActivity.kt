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

    private fun toggleFun4(){
        GlobalScope.launch {
            val avatar = async {
                delay(1000)
                "打印返回值"
            }
            log("==准备==")
            log(avatar.await())
        }
    }
}
