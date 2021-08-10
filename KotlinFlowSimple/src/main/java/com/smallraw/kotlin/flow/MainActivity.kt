package com.smallraw.kotlin.flow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.jraska.console.Console
import com.smallraw.kotlin.flow.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    /**
     * 在 Android 中，StateFlow 非常适合需要将可变状态保持可观察的类。他不会发送重复数据。
     * 他可以在一些生命周期重新执行的时候获取当前最新的数据。
     */

    /**
     * StateFlow 就是热流，他可以发送任何数据，也可以发送重复数据。
     * 但是他无法根据生命周期来重新推送数据。
     * 如果你当前在订阅中，有数据更新会给予推送。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            /**
             * 在生命周期进入 STARTED 状态时从数据流收集数据，在 STOPED 状态时「挂起」协程
             * 在 View 转为 DESTROYED 状态时取消数据流的收集操作「停止」协程。
             */
            lifecycleScope.launchWhenResumed {
//                viewModel.event
//                    .collect {
//                        Console.writeLine(it)
//                    }
            }

            /**
             * 推荐用法，简单安全。
             */
            viewModel.event
                /**
                 * 在生命周期进入 STARTED 状态时开始重复任务，在 STOPED 状态时「停止」协程
                 */
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    Console.writeLine(it)
                }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 在生命周期进入 STARTED 状态时开始重复任务，在 STOPED 状态时停止
                // 对 expensiveObject 进行操作
            }
        }
    }

    fun onClick(view: View) {
        viewModel.setEvent(binding.editContent.text.trim().toString())
    }
}