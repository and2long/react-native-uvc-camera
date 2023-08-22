# react-native-uvc-camera

UVC Camera for RN, **only for Android!**

## Installation

```sh
npm install @and2long/react-native-uvc-camera
```

## Usage

```ts
import { UVCCamera } from "@and2long/react-native-uvc-camera";

// ...
const camera = useRef<UVCCamera>(null);

<UVCCamera ref={camera} width={windowWidth} height={windowHeight} />

// take photo
const result = await camera.current?.takePhoto();
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
