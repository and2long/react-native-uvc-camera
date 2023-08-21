import * as React from 'react';

import { Button, Dimensions, StyleSheet, View } from 'react-native';
import { UvcCamera } from 'react-native-uvc-camera';
import { NavigationContainer, useNavigation } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import { useRef } from 'react';

const Stack = createNativeStackNavigator();

const windowWidth = Dimensions.get('window').width;
const windowHeight = Dimensions.get('window').height;

export default function App() {
  return (
    <NavigationContainer>
      <Stack.Navigator initialRouteName="HomePage">
        <Stack.Screen name="HomePage" component={HomePage} />
        <Stack.Screen name="CameraPage" component={CameraPage} />
      </Stack.Navigator>
    </NavigationContainer>
  );
}

const HomePage = () => {
  const navigation = useNavigation();
  return (
    <View style={styles.root}>
      <Button
        title="Open CameraView"
        // @ts-ignore
        onPress={() => navigation.navigate('CameraPage')}
      />
    </View>
  );
};

const CameraPage = () => {
  const camera = useRef<UvcCamera>(null);

  return (
    <View style={styles.root}>
      <UvcCamera style={styles.cameraView} ref={camera} />
      <View style={styles.controlBar}>
        <Button
          title="Open Camera"
          onPress={() => camera.current?.openCamera()}
        />
        <Button
          title="Close Camera"
          onPress={() => camera.current?.closeCamera()}
        />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  cameraView: {
    width: windowWidth,
    height: windowHeight,
  },
  controlBar: {
    flexDirection: 'row',
    position: 'absolute',
    bottom: 50,
    gap: 10,
  },
});
