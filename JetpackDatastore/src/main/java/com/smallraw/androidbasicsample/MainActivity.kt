package com.smallraw.androidbasicsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val mDataStore by lazy {
        baseContext.createDataStore(
            name = "settings"
        )
    }
    private val EXAMPLE_COUNTER = preferencesKey<String>("example_counter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        launch(Dispatchers.IO) {
            // 自动关注配置变化
            mDataStore.data
                .map { preferences ->
                    preferences[EXAMPLE_COUNTER] ?: "null"
                }.collect {
                    withContext(Dispatchers.Main) {
                        tvAutoContent.text = it
                    }
                }
        }
        btnRead.setOnClickListener {
            launch(Dispatchers.IO) {
                // 第一种读取方案
//                val edit = mDataStore.edit { }
//                val value = edit[EXAMPLE_COUNTER] ?: "null"
//                withContext(Dispatchers.Main) {
//                    tvContent.text = value
//                }

                // 第二种读取方案
                mDataStore.edit {
                    val value = it[EXAMPLE_COUNTER] ?: "null"
                    withContext(Dispatchers.Main) {
                        tvContent.text = value
                    }
                }
            }
        }
        btnWrite.setOnClickListener {
            launch(Dispatchers.IO) {
                // 第一种写入方式，强烈推荐方式
                mDataStore.edit { settings ->
                    settings[EXAMPLE_COUNTER] = editContent.text.toString()
                }

                // 第二种写入方式，但是这样写入自动关注的配置将不会有变化
//                val edit = mDataStore.edit {} as MutablePreferences
//                edit[EXAMPLE_COUNTER] = editContent.text.toString()
            }
        }
    }
}