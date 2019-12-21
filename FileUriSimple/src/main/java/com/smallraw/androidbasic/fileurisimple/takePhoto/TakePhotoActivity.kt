package com.smallraw.androidbasic.fileurisimple.takePhoto

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sl.utakephoto.exception.TakeException
import com.sl.utakephoto.manager.ITakePhotoResult
import com.sl.utakephoto.manager.UTakePhoto
import com.smallraw.androidbasic.fileurisimple.R
import com.smallraw.androidbasic.fileurisimple.utils.copySteam
import com.smallraw.androidbasic.fileurisimple.utils.getNewFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import kotlin.random.Random


class TakePhotoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_photo)

        btnTakePhoto.setOnClickListener {
            UTakePhoto.with(this).openCamera().build(object : ITakePhotoResult {
                override fun takeFailure(ex: TakeException?) {

                }

                override fun takeSuccess(uriList: MutableList<Uri>?) {
                    try {
                        uriList?.get(0)?.let {
                            val file = getRealPathFromURI(it)

                            Log.e("==file==", file.absolutePath)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun takeCancel() {

                }
            })
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): File {
        // Uri 是资源路径，他与 Http 的 Url 类似。
        // 从相册以及拍照获取的 Uri 他是资源。
        // 因为他本身就不是一个文件,把他当成字节流存储到本地再当作 File 使用。
        val openInputStream = contentResolver.openInputStream(contentURI)
        val outputFile = getNewFile(applicationContext, contentURI)
        copySteam(openInputStream, outputFile.outputStream())
        return outputFile
    }
}
