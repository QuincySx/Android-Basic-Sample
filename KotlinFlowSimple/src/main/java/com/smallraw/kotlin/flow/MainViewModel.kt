package com.smallraw.kotlin.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _event = MutableStateFlow("InitialState")

    val event: StateFlow<String> = _event

    fun setEvent(event: String) {
        _event.tryEmit(event)
    }

    init {
        GlobalScope.launch {
            while (true){
                delay(1000)
                setEvent("心跳"+System.currentTimeMillis())
            }
        }
    }
}