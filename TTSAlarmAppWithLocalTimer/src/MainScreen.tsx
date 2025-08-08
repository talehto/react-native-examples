import React, { useState } from 'react';
import { Button, View, TextInput, NativeModules, Platform } from 'react-native';
//import { setAlarm } from './AlarmSetter';

export default function MainScreen() {

  const [customText, setCustomText] = useState('');

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
