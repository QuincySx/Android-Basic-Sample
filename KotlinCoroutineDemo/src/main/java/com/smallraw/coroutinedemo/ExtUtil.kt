package com.smallraw.coroutinedemo

import android.util.Log

fun log(msg: String) {
    Log.e("Coroutine Log", "msg:${msg}   thread:[${Thread.currentThread().name}]")
}