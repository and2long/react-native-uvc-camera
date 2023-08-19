package com.uvccamera

import android.graphics.Color
import android.view.View
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp

class UvcCameraViewManager : SimpleViewManager<CameraView>() {
  override fun getName() = "UvcCameraView"

  override fun createViewInstance(reactContext: ThemedReactContext): CameraView {
    return CameraView(reactContext)
  }

}
