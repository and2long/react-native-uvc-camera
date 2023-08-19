package com.uvccamera

import android.content.pm.PackageManager
import android.util.Log
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.ReadableMap

class UvcCameraViewModule(reactContext: ReactApplicationContext?) :
  ReactContextBaseJavaModule(reactContext) {
  companion object {
    const val TAG = "UvcCameraView"
  }

  override fun getName() = "UvcCameraView"

}
