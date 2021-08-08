package com.smallraw.androidbasic.customview

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smallraw.androidbasic.customview.databinding.ActivityMainBinding
import com.smallraw.androidbasic.customview.mappingProcess.MappingProcessActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnMappingProcess.setOnClickListener {
            startActivity(Intent(this@MainActivity, MappingProcessActivity::class.java))
        }
    }
}
