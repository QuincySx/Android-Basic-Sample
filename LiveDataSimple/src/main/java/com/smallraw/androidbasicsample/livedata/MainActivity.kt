package com.smallraw.androidbasicsample.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    data class TestObject(var path: String)

    val liveData = MutableLiveData<TestObject?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        liveData.observe(this, {
            Log.e("LiveData", "receive data: ${it?.path}")
        })

        launch(Dispatchers.IO) {
            val oldObject = TestObject("old")
            delay(1000)
            liveData.postValue(oldObject)

            delay(1000)
            liveData.postValue(oldObject)

            delay(1000)
            oldObject.path="old-new"
            liveData.postValue(oldObject)

            delay(1000)
            val newObject = TestObject("new")
            liveData.postValue(newObject)
        }
    }
}