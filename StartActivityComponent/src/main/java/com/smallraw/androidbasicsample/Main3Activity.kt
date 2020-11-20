package com.smallraw.androidbasicsample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Main3Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        setResult(RESULT_OK, Intent().apply {
            putExtra("EXT","result from main3")
        })
    }
}
