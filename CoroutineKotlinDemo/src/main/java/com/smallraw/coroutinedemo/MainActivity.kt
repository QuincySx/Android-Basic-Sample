package com.smallraw.coroutinedemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(v: View?) {
        val clazz = when (v?.id) {
            R.id.btnStartCoroutine -> {
                // 启动写成示例
                StartCoroutinesActivity::class.java
            }
            R.id.btnToggleCoroutine -> {
                // 切换协程示例
                ToggleCoroutinesActivity::class.java
            }
            R.id.btnCiroutinersCooperationActivity -> {
                // 协程结构化以及多协程协作配合
                CiroutinersCooperationActivity::class.java
            }
            R.id.btnCancelCoroutinesActivity -> {
                // 取消协程以及协程超时
                CancelCoroutinesActivity::class.java
            }
            R.id.btnCoroutineContextActivity -> {
                // 协程上下文与线程调度器
                CoroutineContextActivity::class.java
            }
            else -> {
                MainActivity::class.java
            }
        }
        startActivity(Intent(this, clazz))
    }
}
