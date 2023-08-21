import React from 'react';
import {
  NativeModules,
  findNodeHandle,
  requireNativeComponent,
  type NativeMethods,
} from 'react-native';
import type { UvcCameraProps } from './UvcCameraProps';

const CameraModule = NativeModules.UvcCameraView;
if (CameraModule == null) {
  console.error("Camera: Native Module 'UvcCameraView' was null!");
}

const ComponentName = 'UvcCameraView';

type NativeUvcCameraViewProps = UvcCameraProps;
const NativeUvcCameraView =
  requireNativeComponent<NativeUvcCameraViewProps>(ComponentName);
type RefType = React.Component<NativeUvcCameraViewProps> &
  Readonly<NativeMethods>;

export class UvcCamera extends React.PureComponent<UvcCameraProps> {
  private readonly ref: React.RefObject<RefType>;

  constructor(props: UvcCameraProps) {
    super(props);
    this.ref = React.createRef<RefType>();
  }

  private get handle(): number | null {
    return findNodeHandle(this.ref.current);
  }

  public render(): React.ReactNode {
    return <NativeUvcCameraView {...this.props} ref={this.ref} />;
  }

  public async openCamera(): Promise<void> {
    await CameraModule.openCamera(this.handle);
  }

  public async closeCamera(): Promise<void> {
    await CameraModule.closeCamera(this.handle);
  }
}
