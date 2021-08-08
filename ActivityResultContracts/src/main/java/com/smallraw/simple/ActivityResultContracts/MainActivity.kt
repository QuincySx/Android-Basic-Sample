package com.smallraw.simple.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<View>(R.id.btnOpenPage).setOnClickListener {

        }
        findViewById<View>(R.id.btnOpenCamera).setOnClickListener {

        }
        findViewById<View>(R.id.btnReuqestPermissions).setOnClickListener {

        }
    }
}