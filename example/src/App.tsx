import * as React from 'react';

import { Dimensions, StyleSheet, View } from 'react-native';
import { UvcCameraView } from 'react-native-uvc-camera';

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default function App() {
  return (
    <View style={styles.container}>
      <UvcCameraView style={styles.box} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: windowWidth,
    height: windowHeight,
  },
});
