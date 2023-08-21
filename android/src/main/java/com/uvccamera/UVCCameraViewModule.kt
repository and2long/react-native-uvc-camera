package com.uvccamera

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.uimanager.UIManagerHelper

class UVCCameraViewModule(reactContext: ReactApplicationContext?) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName() = TAG

  private fun findCameraView(viewId: Int): UVCCameraView {
    Log.d(TAG, "Finding view $viewId...")
    val view = if (reactApplicationContext != null) {
      UIManagerHelper.getUIManager(reactApplicationContext, viewId)
        ?.resolveView(viewId) as UVCCameraView?
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
