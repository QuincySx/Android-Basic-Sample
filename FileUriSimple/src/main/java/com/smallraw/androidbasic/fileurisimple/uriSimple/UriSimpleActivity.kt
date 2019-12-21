package com.smallraw.androidbasic.fileurisimple.uriSimple

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.FileProvider
import com.smallraw.androidbasic.fileurisimple.BuildConfig
import com.smallraw.androidbasic.fileurisimple.R
import com.smallraw.androidbasic.fileurisimple.utils.createFile
import com.smallraw.androidbasic.fileurisimple.utils.getNewFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.Exception
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class UriSimpleActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uri_simple)
        launch(Dispatchers.IO) {
            try {
                val newFile = getNewFile(applicationContext)
                val newFileUri = Uri.fromFile(newFile)
                // Android 7.0 之后不允许 file 的 Uri 在 App 之间传递
                // 可以使用 FileProvider 转换一下
                val fileProviderFileUri = FileProvider.getUriForFile(
                    applicationContext,
                    "${BuildConfig.APPLICATION_ID}.fileProvider",
                    newFile
                )

                val newFile2 = File(getExternalFilesDir(null), "test_child/cache/images/abc.jpg")
                createFile(newFile2)
                val fileProviderFileUri2 = FileProvider.getUriForFile(
                    applicationContext,
                    "${BuildConfig.APPLICATION_ID}.fileProvider",
                    newFile2
                )
                Log.e("==newFileUri==", "${newFileUri.path}")
//                Log.e("==fileProviderFileUri==", "${fileProviderFileUri.path}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // [scheme:]scheme-specific-part[#fragment]
        // file:///storage/emulated/0/Android/data/com.smallraw.androidbasic.fileurisimple/cache/images/Pictures/20191221175237.jpg
        // content://com.smallraw.androidbasic.fileurisimple.fileProvider/images/Pictures/20191221175623.jpg
    }
}
