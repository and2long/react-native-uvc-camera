package com.uvccamera

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.Log
import android.view.SurfaceHolder
import android.widget.FrameLayout
import com.facebook.react.bridge.ReactContext
import com.herohan.uvcapp.CameraHelper
import com.herohan.uvcapp.ICameraHelper
import com.serenegiant.widget.AspectRatioSurfaceView

const val TAG = "UVCCameraView"

class UVCCameraView(context: Context) : FrameLayout(context) {

  companion object {
    private const val DEBUG = true
    private const val DEFAULT_WIDTH = 720
    private const val DEFAULT_HEIGHT = 1280
  }

  var mCameraHelper: ICameraHelper? = null
  private val mCameraViewMain: AspectRatioSurfaceView

  private val reactContext: ReactContext
    get() = context as ReactContext

  init {
    mCameraViewMain = AspectRatioSurfaceView(reactContext)
    mCameraViewMain.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
//    mCameraViewMain.setAspectRatio(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    mCameraViewMain.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated() called with: holder = $holder")
        mCameraHelper?.addSurface(holder.surface, false)
        initCameraHelper()
      }

      override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed() called with: holder = $holder")
        mCameraHelper?.removeSurface(holder.surface)
        clearCameraHelper()
      }
    })
    addView(mCameraViewMain)

  }

  private val mStateListener: ICameraHelper.StateCallback = object : ICameraHelper.StateCallback {
    override fun onAttach(device: UsbDevice) {
      if (DEBUG) Log.v(TAG, "onAttach:")
      selectDevice(device)
    }

    override fun onDeviceOpen(device: UsbDevice, isFirstOpen: Boolean) {
      if (DEBUG) Log.v(TAG, "onDeviceOpen:")
      mCameraHelper?.openCamera()
    }

    override fun onCameraOpen(device: UsbDevice) {
      if (DEBUG) Log.v(TAG, "onCameraOpen:")
      mCameraHelper?.run {
        startPreview()
        addSurface(mCameraViewMain.holder.surface, false)
      }
    }

    override fun onCameraClose(device: UsbDevice) {
      if (DEBUG) Log.v(TAG, "onCameraClose:")
      mCameraHelper?.removeSurface(mCameraViewMain.holder.surface)
    }

    override fun onDeviceClose(device: UsbDevice) {
      if (DEBUG) Log.v(TAG, "onDeviceClose:")
    }

    override fun onDetach(device: UsbDevice) {
      if (DEBUG) Log.v(TAG, "onDetach:")
    }

    override fun onCancel(device: UsbDevice) {
      if (DEBUG) Log.v(TAG, "onCancel:")
    }
  }

  private fun selectDevice(device: UsbDevice) {
    if (DEBUG) Log.v(TAG, "selectDevice:device=" + device.deviceName)
    mCameraHelper?.selectDevice(device)
  }

  private fun initCameraHelper() {
    if (DEBUG) Log.d(TAG, "initCameraHelper:")
    mCameraHelper = CameraHelper().apply {
      setStateCallback(mStateListener)
      previewConfig.rotation = 90
    }
  }

  private fun clearCameraHelper() {
    if (DEBUG) Log.d(TAG, "clearCameraHelper:")
    mCameraHelper?.release()
    mCameraHelper = null
  }

  fun openCamera() {
    mCameraHelper?.run {
      if (deviceList != null && deviceList.size > 0) {
        selectDevice(deviceList[0])
      }
    }
  }

  fun closeCamera() {
    mCameraHelper?.closeCamera()
  }
}
