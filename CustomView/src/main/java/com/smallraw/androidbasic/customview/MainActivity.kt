package com.smallraw.androidbasic.customview

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smallraw.androidbasic.customview.mappingProcess.MappingProcessActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnMappingProcess.setOnClickListener {
            startActivity(Intent(this@MainActivity, MappingProcessActivity::class.java))
        }
    }
}
