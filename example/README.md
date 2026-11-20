# UVC Camera Android Example

Use an Android device with USB host support and connect a UVC camera. The build
environment is listed in the [contributing guide](../CONTRIBUTING.md#development-workflow).

From the repository root, install the library and example dependencies:

```sh
pnpm install --frozen-lockfile
pnpm --dir example install --frozen-lockfile
```

Start Metro in one terminal:

```sh
pnpm example start
```

From a second terminal at the repository root, build and launch the app:

```sh
pnpm example android
```

In the app, tap **Open CameraView**, grant Android camera and USB access when
prompted, and wait for the preview. Tap **Take Photo** to capture a JPEG and see
its thumbnail. Use **Close Camera** and **Open Camera** to stop and restart the
preview.
