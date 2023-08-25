# react-native-uvc-camera

UVC Camera component for Reat Native.

**Works only on Android!**


This is a wrapper for the [UVCAndroid](https://github.com/shiyinghan/UVCAndroid) library.

## Installation

```sh
npm install @and2long/react-native-uvc-camera
```

## Usage
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
