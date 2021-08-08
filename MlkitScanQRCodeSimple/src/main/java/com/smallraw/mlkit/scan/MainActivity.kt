package com.smallraw.mlkit.scan

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Size
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.jraska.console.Console
import com.smallraw.mlkit.scan.barcode.core.QRCodeFoundListener
import com.smallraw.mlkit.scan.barcode.core.QRCodeImageAnalyzer
import com.smallraw.mlkit.scan.databinding.ActivityMainBinding
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors


class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    private val cameraExecutor by lazy {
        Executors.newSingleThreadExecutor()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun startCamera() {

        val previewView = binding.viewFinder
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture?.addListener({
            try {
                val cameraProvider = cameraProviderFuture?.get()
                val preview = Preview.Builder()
                    .build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                val cameraSelector =
                    CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                val imageAnalysis = ImageAnalysis.Builder()
                    .setTargetRotation(preview.targetRotation)
                    .setTargetResolution(Size(previewView.width, previewView.height))
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                cameraProvider?.bindToLifecycle(
                    this,
                    cameraSelector,
                    imageAnalysis,
                    preview
                )
                imageAnalysis.setAnalyzer(
                    cameraExecutor,
                    QRCodeImageAnalyzer(object : QRCodeFoundListener {
                        override fun onQRCodeFound(qrCode: String?) {
                            Console.writeLine("QRCode content: $qrCode")
                            Thread.sleep(500)
                        }

                        override fun onCodeNotFound() {
                            println("not found")
                        }

                        override fun onFailure(e: Exception?) {
                            println(e?.message)
                        }
                    })
                )
            } catch (e: InterruptedException) {
                // Currently no exceptions thrown. cameraProviderFuture.get()
                // shouldn't block since the listener is being called, so no need to
                // handle InterruptedException.
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}