package com.smallraw.androidbasicsample

import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnExplicit.setOnClickListener {
            // 启动原理，根据包名和 Activity 的类名启动

//            explicitStart1()
//            explicitStart2()
            explicitStart3()
        }

        btnImplicit.setOnClickListener {
            //            implicitStart1()
            checkImplicitStart()
        }
        btnLink.setOnClickListener {
//            val intent1 = Intent(Intent.ACTION_VIEW, Uri.parse("one://openview.com"))
            val intent1 = Intent(Intent.ACTION_VIEW, Uri.parse("two://openview.com"))
            startActivity(intent1)
        }
    }

    private fun explicitStart1() {
        // 第一种显式启动
        val intent1 = Intent(this, Main2Activity::class.java)
        startActivity(intent1)
    }

    private fun explicitStart2() {
        // 第二种显式启动
        val intent2 = Intent()
//            val componentName = ComponentName(this, Main2Activity::class.java)
//            val componentName = ComponentName(this, "com.smallraw.androidbasicsample.Main2Activity")
        val componentName = ComponentName(
            "com.smallraw.androidbasicsample",
            "com.smallraw.androidbasicsample.Main2Activity"
        )
        intent2.component = componentName
        startActivity(intent2)
    }

    private fun explicitStart3() {
        // 第三种显式启动
        val intent3 = Intent()
//            intent3.setClass(this, Main2Activity::class.java)
//            intent3.setClassName(this, "com.smallraw.androidbasicsample.Main2Activity")
        intent3.setClassName(
            "com.smallraw.androidbasicsample",
            "com.smallraw.androidbasicsample.Main2Activity"
        )
        startActivity(intent3)
    }

    private fun implicitStart1() {
        // 第一种隐式启动
//        val intent = Intent("startAction")
//        startActivity(intent)

        // or
        val intent1 = Intent()
        intent1.setAction("startAction")
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent1)
    }

    private fun checkImplicitStart() {
        Intent.ACTION_VIEW
        val intent1 = Intent("startAction")
        if (intent1.resolveActivity(getPackageManager()) != null) {
            startActivity(intent1)
            Toast.makeText(this, "True", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "False", Toast.LENGTH_SHORT).show()
        }

    }
}
