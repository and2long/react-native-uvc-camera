package com.uvccamera

import android.content.Context
import android.hardware.usb.UsbDevice
import android.util.Log
import android.view.SurfaceHolder
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.facebook.react.bridge.LifecycleEventListener
import com.facebook.react.bridge.ReactContext
import com.herohan.uvcapp.CameraHelper
import com.herohan.uvcapp.ICameraHelper
import com.serenegiant.widget.AspectRatioSurfaceView

class CameraView(context: Context) : FrameLayout(context), LifecycleOwner {

  companion object {
    private const val DEBUG = true
    private val TAG = CameraView::class.java.simpleName
    private const val DEFAULT_WIDTH = 640
    private const val DEFAULT_HEIGHT = 480
  }

  private var mCameraHelper: ICameraHelper? = null
  private val mCameraViewMain: AspectRatioSurfaceView
  private val lifecycleRegistry: LifecycleRegistry
  private var hostLifecycleState: Lifecycle.State
  var isActive = true

  private val reactContext: ReactContext
    get() = context as ReactContext

  init {
    mCameraViewMain = AspectRatioSurfaceView(reactContext)
    mCameraViewMain.layoutParams =
      LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
    mCameraViewMain.setAspectRatio(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    mCameraViewMain.holder.addCallback(object : SurfaceHolder.Callback {
      override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceCreated() called with: holder = $holder")
        mCameraHelper?.addSurface(holder.surface, false)
      }

      override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
      }

      override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "surfaceDestroyed() called with: holder = $holder")
        mCameraHelper?.removeSurface(holder.surface)
      }
    })
    addView(mCameraViewMain)

    hostLifecycleState = Lifecycle.State.INITIALIZED
    lifecycleRegistry = LifecycleRegistry(this)
    reactContext.addLifecycleEventListener(object : LifecycleEventListener {

      override fun onHostResume() {
        hostLifecycleState = Lifecycle.State.RESUMED
        updateLifecycleState()
        initCameraHelper()
      }

      override fun onHostPause() {
        hostLifecycleState = Lifecycle.State.CREATED
        updateLifecycleState()
        clearCameraHelper()
      }

      override fun onHostDestroy() {
        hostLifecycleState = Lifecycle.State.DESTROYED
        updateLifecycleState()
        reactContext.removeLifecycleEventListener(this)
      }
    })

  }

  private fun updateLifecycleState() {
    val lifecycleBefore = lifecycleRegistry.currentState
    if (hostLifecycleState == Lifecycle.State.RESUMED) {
      // Host Lifecycle (Activity) is currently active (RESUMED), so we narrow it down to the view's lifecycle
      if (isActive && isAttachedToWindow) {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
      } else {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
      }
    } else {
      // Host Lifecycle (Activity) is currently inactive (STARTED or DESTROYED), so that overrules our view's lifecycle
      lifecycleRegistry.currentState = hostLifecycleState
    }
    Log.d(
      TAG,
      "Lifecycle went from ${lifecycleBefore.name} -> ${lifecycleRegistry.currentState.name} (isActive: $isActive | isAttachedToWindow: $isAttachedToWindow)"
    )
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
      mCameraHelper?.startPreview()
      val size = mCameraHelper!!.previewSize
      if (size != null) {
        val width = size.width
        val height = size.height
        //auto aspect ratio
        mCameraViewMain.setAspectRatio(width, height)
      }
      mCameraHelper!!.addSurface(mCameraViewMain.holder.surface, false)
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
    mCameraHelper = CameraHelper()
    mCameraHelper!!.setStateCallback(mStateListener)
  }

  private fun clearCameraHelper() {
    if (DEBUG) Log.d(TAG, "clearCameraHelper:")
    mCameraHelper?.release()
    mCameraHelper = null
  }

  override fun getLifecycle(): Lifecycle {
    return lifecycleRegistry
  }
}
