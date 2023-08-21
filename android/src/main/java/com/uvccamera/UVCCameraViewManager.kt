package com.uvccamera

import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext

class UVCCameraViewManager : SimpleViewManager<UVCCameraView>() {
  override fun getName() = TAG

  override fun createViewInstance(reactContext: ThemedReactContext): UVCCameraView {
    return UVCCameraView(reactContext)
  }

}
