package com.smallraw.example.androidclasstest

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dalvik.system.DexClassLoader
import java.io.File
import java.io.FileOutputStream
// 补丁制作
// 1. javac SayHotFix.java ISay.java
// 2.1. cd ~/.android-sdk/build-tools/29.0.3
// 2.2. ./d8 SayHotFix.class ISay.class
// 会编译成 hotfix.dex
class MainActivity : AppCompatActivity() {
    var say: ISay? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        say = SayException()
    }

    fun onClick(view: View) {
//        Toast.makeText(this, say?.saySomething() ?: "not find", Toast.LENGTH_SHORT).show()

        val hotfixFile = File(cacheDir.path + File.separator + "hotfix.dex")

        copyFilesFassets(this, "hotfix.dex", hotfixFile.absolutePath)

        if (!hotfixFile.exists()) {
            Toast.makeText(this, "没有文件读取权限", Toast.LENGTH_SHORT).show()
        } else {
            try {
                val dexClassLoader = DexClassLoader(
                    hotfixFile.absolutePath,
                    externalCacheDir?.absolutePath,
                    null,
                    classLoader
                )
                val loadClass =
                    dexClassLoader.loadClass("com.smallraw.example.androidclasstest.SayHotFix")
                val iSay = loadClass.newInstance() as ISay
                Toast.makeText(this, iSay.saySomething() ?: "not find", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun copyFilesFassets(
        context: Context,
        assetsPath: String,
        newPath: String
    ) {
        try {
            val fileNames = context.assets.list(assetsPath) //获取assets目录下的所有文件及目录名
            if (fileNames != null && fileNames.isNotEmpty()) { //如果是目录
                val file = File(newPath)
                file.mkdirs() //如果文件夹不存在，则递归
                for (fileName in fileNames) {
                    copyFilesFassets(
                        context,
                        assetsPath + File.separator + fileName,
                        newPath + File.separator + fileName
                    )
                }
            } else { //如果是文件
                context.assets.open(assetsPath).use { input ->
                    FileOutputStream(File(newPath)).use { output ->
                        input.copyTo(output)
                    }
                }
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
}