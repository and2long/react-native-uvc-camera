package com.uvccamera

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.UIManagerHelper

class UvcCameraViewModule(reactContext: ReactApplicationContext?) :
  ReactContextBaseJavaModule(reactContext) {
  companion object {
    const val TAG = "UvcCameraView"
  }

  override fun getName() = "UvcCameraView"

  private fun findCameraView(viewId: Int): CameraView {
    Log.d(TAG, "Finding view $viewId...")
    val view = if (reactApplicationContext != null) {
      UIManagerHelper.getUIManager(reactApplicationContext, viewId)
        ?.resolveView(viewId) as CameraView?
    } else null
    Log.d(
      TAG,
      if (reactApplicationContext != null) "Found view $viewId!" else "Couldn't find view $viewId!"
    )
    return view ?: throw ViewNotFoundError(viewId)
  }

  @ReactMethod
  fun openCamera(viewTag: Int) {
    val view = findCameraView(viewTag)
    view.openCamera()
  }

  @ReactMethod
  fun closeCamera(viewTag: Int) {
    val view = findCameraView(viewTag)
    view.closeCamera()
  }

}
