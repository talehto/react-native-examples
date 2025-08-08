import React, { useState, useEffect } from 'react';
import { Button, View, TextInput, NativeModules, Platform, PermissionsAndroid } from 'react-native';
//import { setAlarm } from './AlarmSetter';

export default function MainScreen() {

  const [customText, setCustomText] = useState('');

  useEffect(() => {
    requestNotificationPermission();
  }, []);

  const requestNotificationPermission = async () => {
    if (Platform.OS === 'android' && Platform.Version >= 33) {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.POST_NOTIFICATIONS,
          {
            title: "Notification Permission",
            message: "This app needs notification permission to show alarm notifications.",
            buttonNeutral: "Ask Me Later",
            buttonNegative: "Cancel",
            buttonPositive: "OK"
          }
        );
        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
          console.log("Notification permission granted");
        } else {
          console.log("Notification permission denied");
        }
      } catch (err) {
        console.warn(err);
      }
    }
  };

  const handleSetAlarm = () => {
    console.log("handleSetAlarm called")
    const now = new Date().getTime();
    const inFiveSeconds = now + 5000;
    //setAlarm(inFiveSeconds);
    NativeModules.AlarmModule.setAlarm(inFiveSeconds, customText);
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center', padding: 20 }}>
      <TextInput
        placeholder="Anna oma herätysviesti (valinnainen)"
        value={customText}
        onChangeText={setCustomText}
        style={{
          borderWidth: 1,
          borderColor: '#ccc',
          padding: 10,
          marginBottom: 20,
          width: '100%'
        }}
      />
      <Button title="Aseta TTS-hälytys 5 sek kuluttua" onPress={handleSetAlarm} />
    </View>
  );
}
