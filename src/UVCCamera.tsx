import React from 'react';
import {
  NativeModules,
  findNodeHandle,
  requireNativeComponent,
  type HostInstance,
} from 'react-native';
import type { UVCCameraProps } from './UVCCameraProps';
import type { PhotoFile } from './PhotoFile';

const CameraModule = NativeModules.UVCCameraView;
if (CameraModule == null) {
  console.error("Camera: Native Module 'UVCCameraView' was null!");
}

const ComponentName = 'UVCCameraView';

type NativeUVCCameraViewProps = UVCCameraProps;
const NativeUVCCameraView =
  requireNativeComponent<NativeUVCCameraViewProps>(ComponentName);
export class UVCCamera extends React.PureComponent<UVCCameraProps> {
  private readonly ref: React.RefObject<HostInstance | null>;

  constructor(props: UVCCameraProps) {
    super(props);
    this.ref = React.createRef<HostInstance>();
  }

  private get handle(): number {
    const handle = findNodeHandle(this.ref.current);
    if (handle == null) {
      throw new Error(
        'Camera view must be mounted before calling camera methods.'
      );
    }
    return handle;
  }

  public render(): React.ReactNode {
    return <NativeUVCCameraView {...this.props} ref={this.ref} />;
  }

  public async openCamera(): Promise<void> {
    await CameraModule.openCamera(this.handle);
  }

  public async closeCamera(): Promise<void> {
    await CameraModule.closeCamera(this.handle);
  }

  public async takePhoto(): Promise<PhotoFile> {
    return await CameraModule.takePhoto(this.handle);
  }
}
