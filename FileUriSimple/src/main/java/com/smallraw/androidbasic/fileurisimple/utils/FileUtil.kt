package com.smallraw.androidbasic.fileurisimple.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import kotlin.math.absoluteValue

// 创建多层级目录
// 创建父目录
fun getNewFile(context: Context, uri: Uri? = null): File {
    // Kotlin 闭包函数
    val newFile = File("${context.externalCacheDir?.absolutePath}${File.separator}${uri?.path ?: {
        val random = SecureRandom().nextInt().absoluteValue
        "/test_files/$random.jpeg"
    }()}")
    createFile(newFile)
    return newFile
}

fun createFile(file: File) {
    val parentFile = file.parentFile
    if (parentFile?.exists() == false) {
        parentFile.mkdirs()
    }
    if (!file.exists()) {
        file.createNewFile()
    }
}

// 大多数人使用2的幂作为大小。如果缓冲区至少为512字节，则差别不大（<20％）
//
// 对于网络，最佳大小可以为2 KB至8 KB（底层数据包大小通常约为1.5 KB）。对于磁盘访问，最快的大小可以为8K至64 KB。如果您使用8K或16K，则不会有问题。
//
// 请注意，对于网络下载，您可能会发现通常不使用整个缓冲区。对于99％的用例而言，浪费几KB都没有多大关系
fun copySteam(`in`: InputStream?, out: OutputStream) {
    if (`in` == null) {
        return
    }
    `in`.use { input ->
        out.use { output ->
            val buff = ByteArray(4 * 1024)
            var read: Int
            while ({ read = input.read(buff);read }() != -1) {
                output.write(buff)
            }
        }
    }
}