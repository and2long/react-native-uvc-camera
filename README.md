# react-native-uvc-camera

UVC Camera component for React Native.

**Android only.**

This is a wrapper for the [UVCAndroid](https://github.com/shiyinghan/UVCAndroid) library.

## Installation

To install the published package:

```sh
npm install @and2long/react-native-uvc-camera
```

## Usage

The library declares `android.permission.CAMERA` in its Android manifest. Request
that permission at runtime before rendering `UVCCamera`. Connect a UVC camera to
your Android device and accept the USB access prompt when Android shows it. The
view starts the preview automatically when the USB camera opens.

```tsx
import { useRef, useState } from 'react';
import { Button, Image, PermissionsAndroid, Text, View } from 'react-native';
import { UVCCamera } from '@and2long/react-native-uvc-camera';

export default function CameraScreen() {
  const camera = useRef<UVCCamera>(null);
  const [granted, setGranted] = useState(false);
  const [photoUri, setPhotoUri] = useState<string>();
  const [error, setError] = useState('');

  async function requestPermission() {
    setError('');
    try {
      const result = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.CAMERA
      );
      if (result === PermissionsAndroid.RESULTS.GRANTED) {
        setGranted(true);
      } else {
        setError('Allow camera access to use the USB camera.');
      }
    } catch {
      setError('Could not request camera permission.');
    }
  }

  async function takePhoto() {
    if (!camera.current) return;

    setError('');
    try {
      const photo = await camera.current.takePhoto();
      setPhotoUri(`file://${photo.path}`);
    } catch {
      setError('Wait for the camera preview, then try taking the photo again.');
    }
  }

  return (
    <View>
      {granted ? (
        <>
          <UVCCamera ref={camera} style={{ width: 320, height: 240 }} />
          <Button title="Take photo" onPress={takePhoto} />
        </>
      ) : (
        <Button title="Enable camera" onPress={requestPermission} />
      )}
      {error ? <Text>{error}</Text> : null}
      {photoUri && (
        <Image source={{ uri: photoUri }} style={{ width: 160, height: 120 }} />
      )}
    </View>
  );
}
```

Tap **Take photo** once the preview is visible. `takePhoto()` returns `{ path }`,
the absolute path to a JPEG in the app's private cache. Use a `file://` URI to
display the image, and copy the file to persistent storage if you need to keep it.
Capture works with the camera and USB permissions alone.

The mounted camera ref also exposes `openCamera()` and `closeCamera()` for
manually controlling the preview.

To try the library directly from this checkout, see the [Android example app](example/README.md).

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
