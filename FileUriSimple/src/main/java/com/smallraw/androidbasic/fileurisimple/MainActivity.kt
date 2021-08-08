package com.smallraw.androidbasic.fileurisimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smallraw.androidbasic.fileurisimple.databinding.ActivityMainBinding
import com.smallraw.androidbasic.fileurisimple.takePhoto.TakePhotoActivity
import com.smallraw.androidbasic.fileurisimple.uriSimple.UriSimpleActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnTakePhoto.setOnClickListener {
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }
        binding.btnUriSimple.setOnClickListener {
            val intent = Intent(this, UriSimpleActivity::class.java)
            startActivity(intent)
        }
    }
}
