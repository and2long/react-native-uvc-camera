import React from 'react';
import {
  NativeModules,
  findNodeHandle,
  requireNativeComponent,
  type NativeMethods,
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
type RefType = React.Component<NativeUVCCameraViewProps> &
  Readonly<NativeMethods>;

export class UVCCamera extends React.PureComponent<UVCCameraProps> {
  private readonly ref: React.RefObject<RefType>;

  constructor(props: UVCCameraProps) {
    super(props);
    this.ref = React.createRef<RefType>();
  }

  private get handle(): number | null {
    return findNodeHandle(this.ref.current);
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
  public async updateAspectRatio(width: number, height: number): Promise<void> {
    await CameraModule.updateAspectRatio(this.handle, width, height);
  }
}
