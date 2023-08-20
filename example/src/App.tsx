import * as React from 'react';

import { Button, Dimensions, StyleSheet, View } from 'react-native';
import { UvcCameraView } from 'react-native-uvc-camera';
import { NavigationContainer, useNavigation } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

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
        onPress={() => navigation.navigate('CameraPage')}
      />
    </View>
  );
};

const CameraPage = () => {
  return (
    <View style={styles.root}>
      <UvcCameraView style={styles.box} />
    </View>
  );
};

const styles = StyleSheet.create({
  root: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: windowWidth,
    height: windowHeight,
  },
});
