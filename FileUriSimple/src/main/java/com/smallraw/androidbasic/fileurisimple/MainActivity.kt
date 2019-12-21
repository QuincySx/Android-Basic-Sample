package com.smallraw.androidbasic.fileurisimple

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smallraw.androidbasic.fileurisimple.takePhoto.TakePhotoActivity
import com.smallraw.androidbasic.fileurisimple.uriSimple.UriSimpleActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnTakePhoto.setOnClickListener {
            val intent = Intent(this, TakePhotoActivity::class.java)
            startActivity(intent)
        }
        btnUriSimple.setOnClickListener {
            val intent = Intent(this, UriSimpleActivity::class.java)
            startActivity(intent)
        }
    }
}
