package com.uvccamera

import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.herohan.uvcapp.IImageCapture
import java.io.File
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend inline fun UVCCameraView.takePicture() = suspendCoroutine { cont ->
  // App-private cache works with scoped storage without media/storage permissions.
  val file = File.createTempFile("uvc-", ".jpg", context.cacheDir)
  val options = IImageCapture.OutputFileOptions.Builder(file).build()
  this.mCameraHelper!!.takePicture(
    options,
    object : IImageCapture.OnImageCaptureCallback {
      override fun onImageSaved(outputFileResults: IImageCapture.OutputFileResults) {
        cont.resume(file.absolutePath)
      }

      override fun onError(imageCaptureError: Int, message: String, cause: Throwable?) {
        file.delete()
        cont.resumeWithException(Exception(message))
      }
    }
  )
}

suspend fun UVCCameraView.takePhoto(): WritableMap = coroutineScope {
  val startFunc = System.nanoTime()
  val pic = takePicture()
  Log.d(TAG, "Finished taking photo!")
  val endFunc = System.nanoTime()
  Log.d(TAG, "Finished function execution in ${(endFunc - startFunc) / 1_000_000}ms")

  val map = Arguments.createMap()
  map.putString("path", pic)

  return@coroutineScope map
}
