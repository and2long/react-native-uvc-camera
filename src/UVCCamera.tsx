import React from 'react';
import {
  NativeModules,
  findNodeHandle,
  requireNativeComponent,
  type NativeMethods,
  type NativeSyntheticEvent,
} from 'react-native';
import type {
  FrameProcessorPerformanceSuggestion,
  UVCCameraProps,
} from './UVCCameraProps';
import type { PhotoFile } from './PhotoFile';

const CameraModule = NativeModules.UVCCameraView;
if (CameraModule == null) {
  console.error("Camera: Native Module 'UVCCameraView' was null!");
}

const ComponentName = 'UVCCameraView';

type NativeUVCCameraViewProps = Omit<
  UVCCameraProps,
  'onFrameProcessorPerformanceSuggestionAvailable'
> & {
  onFrameProcessorPerformanceSuggestionAvailable?: (
    event: NativeSyntheticEvent<FrameProcessorPerformanceSuggestion>
  ) => void;
  enableFrameProcessor: boolean;
};
const NativeUVCCameraView =
  requireNativeComponent<NativeUVCCameraViewProps>(ComponentName);
type RefType = React.Component<NativeUVCCameraViewProps> &
  Readonly<NativeMethods>;

export class UVCCamera extends React.PureComponent<UVCCameraProps> {
  private readonly ref: React.RefObject<RefType>;

  constructor(props: UVCCameraProps) {
    super(props);
    this.ref = React.createRef<RefType>();
    this.onFrameProcessorPerformanceSuggestionAvailable =
      this.onFrameProcessorPerformanceSuggestionAvailable.bind(this);
  }

  private get handle(): number | null {
    return findNodeHandle(this.ref.current);
  }

  public render(): React.ReactNode {
    const { frameProcessor, ...props } = this.props;
    return (
      <NativeUVCCameraView
        {...props}
        ref={this.ref}
        onFrameProcessorPerformanceSuggestionAvailable={
          this.onFrameProcessorPerformanceSuggestionAvailable
        }
        enableFrameProcessor={frameProcessor != null}
      />
    );
  }

  private onFrameProcessorPerformanceSuggestionAvailable(
    event: NativeSyntheticEvent<FrameProcessorPerformanceSuggestion>
  ): void {
    if (this.props.onFrameProcessorPerformanceSuggestionAvailable != null)
      this.props.onFrameProcessorPerformanceSuggestionAvailable(
        event.nativeEvent
      );
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
