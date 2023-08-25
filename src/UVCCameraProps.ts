import type { ViewProps } from 'react-native';
import type { Frame } from './Frame';

export interface FrameProcessorPerformanceSuggestion {
  type: 'can-use-higher-fps' | 'should-use-lower-fps';
  suggestedFrameProcessorFps: number;
}

export interface UVCCameraProps extends ViewProps {
  onFrameProcessorPerformanceSuggestionAvailable?: (
    suggestion: FrameProcessorPerformanceSuggestion
  ) => void;
  frameProcessor?: (frame: Frame) => void;
}
