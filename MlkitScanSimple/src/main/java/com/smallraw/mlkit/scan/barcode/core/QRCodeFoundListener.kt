package com.smallraw.mlkit.scan.barcode.core

interface QRCodeFoundListener {
    fun onQRCodeFound(qrCode: String?)
    fun onCodeNotFound()
    fun onFailure(e: Exception?)
}