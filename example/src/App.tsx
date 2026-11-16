import * as React from 'react';

import { NavigationContainer, useNavigation } from '@react-navigation/native';
import {
  createNativeStackNavigator,
  type NativeStackNavigationProp,
} from '@react-navigation/native-stack';
import { useEffect, useRef } from 'react';
import {
  Button,
  Image,
  PermissionsAndroid,
  Platform,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { SafeAreaProvider } from 'react-native-safe-area-context';
import { UVCCamera } from '../../src/UVCCamera';

type StackParamList = {
  HomePage: undefined;
  CameraPage: undefined;
};
const Stack = createNativeStackNavigator<StackParamList>();

export default function App() {
  return (
    <SafeAreaProvider>
      <NavigationContainer>
        <Stack.Navigator initialRouteName="HomePage">
          <Stack.Screen name="HomePage" component={HomePage} />
          <Stack.Screen name="CameraPage" component={CameraPage} />
        </Stack.Navigator>
      </NavigationContainer>
    </SafeAreaProvider>
  );
}

const HomePage = () => {
  const navigation = useNavigation<NativeStackNavigationProp<StackParamList>>();
  return (
    <View style={styles.root}>
      <Button
        title="Open CameraView"
        onPress={() => navigation.navigate('CameraPage')}
      />
    </View>
  );
};

const CameraPage = () => {
  const camera = useRef<UVCCamera>(null);
  const [picPath, setPicPath] = React.useState<string>();
  const [permissionGranted, setPermissionGranted] = React.useState(false);
  const [permissionError, setPermissionError] = React.useState<string>();

  useEffect(() => {
    let active = true;
    if (Platform.OS !== 'android') {
      setPermissionError('USB camera support is available on Android.');
      return;
    }
    PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.CAMERA)
      .then((result) => {
        if (active) {
          setPermissionGranted(result === PermissionsAndroid.RESULTS.GRANTED);
          if (result !== PermissionsAndroid.RESULTS.GRANTED) {
            setPermissionError(
              'Camera permission is required to use a USB camera.'
            );
          }
        }
      })
      .catch((error: Error) => {
        if (active) {
          setPermissionError(error.message);
        }
      });
    return () => {
      active = false;
    };
  }, []);

  const takePhoto = async () => {
    try {
      const photo = await camera.current?.takePhoto();
      if (!photo) {
        return;
      }
      setPicPath(photo.path);
      console.log('photo', photo);
    } catch (error) {
      console.log('error', error);
    }
  };

  return (
    <View style={styles.root}>
      {permissionGranted ? (
        <UVCCamera ref={camera} style={styles.cameraView} />
      ) : (
        <Text>{permissionError ?? 'Requesting camera permission…'}</Text>
      )}
      <View style={styles.controlBar}>
        <Button
          title="Open Camera"
          disabled={!permissionGranted}
          onPress={() => camera.current?.openCamera()}
        />
        <Button
          title="Close Camera"
          disabled={!permissionGranted}
          onPress={() => camera.current?.closeCamera()}
        />
        <Button
          title="Take Photo"
          onPress={takePhoto}
          disabled={!permissionGranted}
        />
      </View>
      {picPath && (
        <Image
          source={{ uri: `file://${picPath}` }}
          style={styles.pic}
          resizeMode="contain"
        />
      )}
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
    position: 'absolute',
    top: 0,
    right: 0,
    bottom: 0,
    left: 0,
  },
  controlBar: {
    position: 'absolute',
    top: 10,
    gap: 10,
  },
  pic: {
    position: 'absolute',
    backgroundColor: '#000',
    width: 100,
    height: 100,
    bottom: 20,
    right: 20,
  },
});
