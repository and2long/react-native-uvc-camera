package com.uvccamera

import android.os.Environment
import android.util.Log
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.WritableMap
import com.herohan.uvcapp.ImageCapture
import com.serenegiant.utils.FileUtils
import com.serenegiant.utils.UriHelper
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend inline fun UVCCameraView.takePicture() = suspendCoroutine { cont ->
  val file = FileUtils.getCaptureFile(context, Environment.DIRECTORY_DCIM, ".jpg")
  val options = ImageCapture.OutputFileOptions.Builder(file).build()
  this.mCameraHelper!!.takePicture(
    options,
    object : ImageCapture.OnImageCaptureCallback {
      override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
        cont.resume(UriHelper.getPath(context, outputFileResults.savedUri)!!)
      }

      override fun onError(imageCaptureError: Int, message: String, cause: Throwable?) {
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
