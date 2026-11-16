# react-native-uvc-camera

UVC Camera component for React Native.

**Works only on Android!**


This is a wrapper for the [UVCAndroid](https://github.com/shiyinghan/UVCAndroid) library.

## Requirements

- React Native 0.88 with React 19.3 (the example uses RN 0.88.1).
- Android 7.0 / API 24 or newer, with USB host support and a UVC camera.
- Android compile/target SDK 37 (Android 17), JDK 17, and Node.js 24 for development.
- UVCAndroid 1.0.13 is included by the native library.

The native view and module use React Native's interoperability layer with the New Architecture enabled.

## Installation

```sh
npm install @and2long/react-native-uvc-camera
```

## Usage

Declare `android.permission.CAMERA` and request it with `PermissionsAndroid.request`
before mounting the camera view. Android also asks for permission to access the USB device.

```ts
import { UVCCamera } from "@and2long/react-native-uvc-camera";

// ...
const camera = useRef<UVCCamera>(null);

<UVCCamera ref={camera} style={styles.cameraView} />

const styles = StyleSheet.create({
  cameraView: {
    width: 200,
    height: 200,
  },
});
```

### Take photo
```
const photo = await camera.current?.takePhoto();
```

Photos are saved to the app's private cache. Copy them to persistent storage if they
need to survive cache cleanup; no shared-storage permission is needed for capture.

## R8 / ProGuard
If you are using R8 the shrinking and obfuscation rules are included automatically.

ProGuard users must manually add the below options.

```
-keep class com.herohan.uvcapp.** { *; }
-keep class com.serenegiant.usb.** { *; }
-keepclassmembers class * implements com.serenegiant.usb.IButtonCallback {*;}
-keepclassmembers class * implements com.serenegiant.usb.IFrameCallback {*;}
-keepclassmembers class * implements com.serenegiant.usb.IStatusCallback {*;}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
