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
                StartCoroutinesActivity::class.java
            }
            R.id.btnToggleCoroutine -> {
                ToggleCoroutinesActivity::class.java
            }
            else -> {
                MainActivity::class.java
            }
        }
        startActivity(Intent(this, clazz))
    }
}
