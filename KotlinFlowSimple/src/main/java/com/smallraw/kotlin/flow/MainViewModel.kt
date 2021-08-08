package com.smallraw.kotlin.flow

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
    private val _event = MutableStateFlow("InitialState")

    val event: StateFlow<String> = _event

    fun setEvent(event: String) {
        _event.tryEmit(event)
    }
}