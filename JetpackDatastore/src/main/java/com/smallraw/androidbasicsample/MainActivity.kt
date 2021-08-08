package com.smallraw.androidbasicsample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.smallraw.androidbasicsample.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        private val EXAMPLE_COUNTER = stringPreferencesKey("example_counter")
        private val TAG = MainActivity::class.java.simpleName
    }

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val dataStore by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnWrite.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                dataStore.edit { settings ->
                    settings[EXAMPLE_COUNTER] = binding.editContent.text.toString()
                }
            }
        }

        dataStore.data
            .map { preferences ->
                preferences[EXAMPLE_COUNTER] ?: "null"
            }
            .asLiveData(Dispatchers.IO)
            .observe(this@MainActivity) {
                Log.e(TAG, "Result of watching DataStore： $it")
            }


        lifecycleScope.launch(Dispatchers.IO) {
            dataStore.data
                .map { preferences ->
                    preferences[EXAMPLE_COUNTER] ?: "null"
                }


            // 自动关注配置变化
            dataStore.data
                .map { preferences ->
                    preferences[EXAMPLE_COUNTER] ?: "null"
                }.collect {
                    withContext(Dispatchers.Main) {
                        binding.tvAutoContent.text = it
                    }
                }
        }
        binding.btnRead.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                // 第一种读取方案
//                val edit = mDataStore.edit { }
//                val value = edit[EXAMPLE_COUNTER] ?: "null"
//                withContext(Dispatchers.Main) {
//                    tvContent.text = value
//                }

                // 第二种读取方案
                dataStore.edit {
                    val value = it[EXAMPLE_COUNTER] ?: "null"
                    withContext(Dispatchers.Main) {
                        binding.tvContent.text = value
                    }
                }
            }
        }
//        btnWrite.setOnClickListener {
//            launch(Dispatchers.IO) {
//                // 第一种写入方式，强烈推荐方式
//                dataStore.edit { settings ->
//                    settings[EXAMPLE_COUNTER] = editContent.text.toString()
//                }
//
//                // 第二种写入方式，但是这样写入自动关注的配置将不会有变化
////                val edit = mDataStore.edit {} as MutablePreferences
////                edit[EXAMPLE_COUNTER] = editContent.text.toString()
//            }
//        }
    }
}