package com.smallraw.coroutinedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smallraw.coroutinedemo.databinding.ActivityConcurrentAndParallelBinding
import kotlinx.coroutines.*
import java.math.BigInteger
import java.net.URL
import java.util.*
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

private val SingleDispatcher = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

class ConcurrentAndParallelActivity : AppCompatActivity(), CoroutineScope by MainScope() {
    private val binding by lazy { ActivityConcurrentAndParallelBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(binding) {
            setContentView(binding.root)
            // ======================
            btnSingleConcurrentNetWork.setOnClickListener {
                singleConcurrentNetWorkTask()
            }
            btnSingleConcurrentCalculationTask.setOnClickListener {
                singleConcurrentCalculationTask()
            }
            btnMultipleConcurrentNetWork.setOnClickListener {
                multipleConcurrentNetWorkTask()
            }
            btnMultipleConcurrentCalculationTask.setOnClickListener {
                multipleConcurrentCalculationTask()
            }
            // ======================
            btnSingleParallelSingleNetWork.setOnClickListener {
                singleParallelSingleNetWorkTask()
            }
            btnSingleParallelSingleCalculationTask.setOnClickListener {
                singleParallelSingleCalculationTask()
            }
            btnSingleParallelMultipleNetWork.setOnClickListener {
                singleParallelMultipleNetWorkTask()
            }
            btnSingleParallelMultipleCalculationTask.setOnClickListener {
                singleParallelMultipleCalculationTask()
            }
            // ======================
            btnMultipleParallelSingleNetWork.setOnClickListener {
                multipleParallelSingleNetWorkTask()
            }
            btnMultipleParallelSingleCalculationTask.setOnClickListener {
                multipleParallelSingleCalculationTask()
            }
            btnMultipleParallelMultipleNetWork.setOnClickListener {
                multipleParallelMultipleNetWorkTask()
            }
            btnMultipleParallelMultipleCalculationTask.setOnClickListener {
                multipleParallelMultipleCalculationTask()
            }
        }
    }

    private fun addLine(msg: String) {
        launch {
            with(binding) {
                tvLog.append("${msg}\n")
                val offset = tvLog.lineCount * tvLog.lineHeight
                if (offset > tvLog.height) {
                    tvLog.scrollTo(0, offset - tvLog.height)
                }
            }
        }
    }

    // =======================================================
    private fun singleConcurrentNetWorkTask() {
        launch(SingleDispatcher) {
            addLine("开始单线程网络并发")
            val time = measureTimeMillis {
                val async1 = async { netWorkTask() }
                val async2 = async { netWorkTask() }
                addLine("network task1 run time ${async1.await()} ms")
                addLine("network task2 run time ${async2.await()} ms")
            }
            addLine("单线程网络并发任务 run time $time ms")
        }
    }

    private fun singleConcurrentCalculationTask() {
        launch(SingleDispatcher) {
            addLine("开始单线程计算并发")
            val time = measureTimeMillis {
                val async1 = async { calculationTask() }
                val async2 = async { calculationTask() }
                addLine("calculation task1 run time ${async1.await()} ms")
                addLine("calculation task2 run time ${async2.await()} ms")
            }
            addLine("单线程计算并发任务 run time $time ms")
        }
    }

    private fun multipleConcurrentNetWorkTask() {
        launch(Dispatchers.IO) {
            addLine("开始多线程网络并发")
            val time = measureTimeMillis {
                val async1 = async { netWorkTask() }
                val async2 = async { netWorkTask() }
                addLine("network task1 run time ${async1.await()} ms")
                addLine("network task2 run time ${async2.await()} ms")
            }
            addLine("多线程网络并发任务 run time $time ms")
        }
    }

    private fun multipleConcurrentCalculationTask() {
        launch(Dispatchers.IO) {
            addLine("开始多线程计算并发")
            val time = measureTimeMillis {
                val async1 = async { calculationTask() }
                val async2 = async { calculationTask() }
                addLine("calculation task1 run time ${async1.await()} ms")
                addLine("calculation task2 run time ${async2.await()} ms")
            }
            addLine("多线程网络并发任务 run time $time ms")
        }
    }

    // =====================================================================

    private fun singleParallelSingleNetWorkTask() {
        launch(SingleDispatcher) {
            addLine("开始单线程协程单线程异步网络并行")
            val time = measureTimeMillis {
                val async1 = async(SingleDispatcher) { netWorkTask() }
                val async2 = async(SingleDispatcher) { netWorkTask() }
                addLine("network task1 run time ${async1.await()} ms")
                addLine("network task2 run time ${async2.await()} ms")
            }
            addLine("单线程协程单线程异步网络并行任务 run time $time ms")
        }
    }

    private fun singleParallelSingleCalculationTask() {
        launch(SingleDispatcher) {
            addLine("开始单线程协程单线程异步计算并行")
            val time = measureTimeMillis {
                val async1 = async(SingleDispatcher) { calculationTask() }
                val async2 = async(SingleDispatcher) { calculationTask() }
                addLine("calculation task1 run time ${async1.await()} ms")
                addLine("calculation task2 run time ${async2.await()} ms")
            }
            addLine("单线程协程单线程异步计算并行任务 run time $time ms")
        }
    }

    private fun singleParallelMultipleNetWorkTask() {
        launch(SingleDispatcher) {
            addLine("开始单线程协程单线程异步网络并行")
            val time = measureTimeMillis {
                val async1 = async(Dispatchers.IO) { netWorkTask() }
                val async2 = async(Dispatchers.IO) { netWorkTask() }
                addLine("network task1 run time ${async1.await()} ms")
                addLine("network task2 run time ${async2.await()} ms")
            }
            addLine("单线程协程单线程异步网络并行任务 run time $time ms")
        }
    }

    private fun singleParallelMultipleCalculationTask() {
        launch(SingleDispatcher) {
            addLine("开始单线程协程单线程异步计算并行")
            val time = measureTimeMillis {
                val async1 = async(Dispatchers.IO) { calculationTask() }
                val async2 = async(Dispatchers.IO) { calculationTask() }
                addLine("calculation task1 run time ${async1.await()} ms")
                addLine("calculation task2 run time ${async2.await()} ms")
            }
            addLine("单线程协程单线程异步计算并行任务 run time $time ms")
        }
    }

    // =================================================================

    private fun multipleParallelSingleNetWorkTask() {
        launch(Dispatchers.IO) {
            addLine("开始多线程协程单线程异步网络并行")
            val time = measureTimeMillis {
                val async1 = async(SingleDispatcher) { netWorkTask() }
                val async2 = async(SingleDispatcher) { netWorkTask() }
                addLine("network task1 run time ${async1.await()} ms")
                addLine("network task2 run time ${async2.await()} ms")
            }
            addLine("多线程协程单线程异步网络并行任务 run time $time ms")
        }
    }

    private fun multipleParallelSingleCalculationTask() {
        launch(Dispatchers.IO) {
            addLine("开始多线程协程单线程异步计算并行")
            val time = measureTimeMillis {
                val async1 = async(SingleDispatcher) { calculationTask() }
                val async2 = async(SingleDispatcher) { calculationTask() }
                addLine("calculation task1 run time ${async1.await()} ms")
                addLine("calculation task2 run time ${async2.await()} ms")
            }
            addLine("多线程协程单线程异步计算并行任务 run time $time ms")
        }
    }

    private fun multipleParallelMultipleNetWorkTask() {
        launch(Dispatchers.IO) {
            addLine("开始多线程协程单线程异步网络并行")
            val time = measureTimeMillis {
                val async1 = async(Dispatchers.IO) { netWorkTask() }
                val async2 = async(Dispatchers.IO) { netWorkTask() }
                addLine("network task1 run time ${async1.await()} ms")
                addLine("network task2 run time ${async2.await()} ms")
            }
            addLine("多线程协程单线程异步网络并行任务 run time $time ms")
        }
    }

    private fun multipleParallelMultipleCalculationTask() {
        launch(Dispatchers.IO) {
            addLine("开始多线程协程单线程异步计算并行")
            val time = measureTimeMillis {
                val async1 = async(Dispatchers.IO) { calculationTask() }
                val async2 = async(Dispatchers.IO) { calculationTask() }
                addLine("calculation task1 run time ${async1.await()} ms")
                addLine("calculation task2 run time ${async2.await()} ms")
            }
            addLine("多线程协程单线程异步计算并行任务 run time $time ms")
        }
    }

    // =======================================================
    private fun netWorkTask(url: String = "https://github.com/"): Long {
        return measureTimeMillis {
            try {
                val url = URL(url)
                val openStream = url.openStream()
                val data = openStream.bufferedReader().readText()
            } catch (e: Exception) {
            }
        }
    }

    private fun calculationTask(): Long {
        return measureTimeMillis {
            BigInteger(1500, Random()).nextProbablePrime()
        }
    }
}