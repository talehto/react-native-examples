import React from 'react';
import { Button, View } from 'react-native';
import { setAlarm } from './AlarmSetter';

export default function MainScreen() {
  const handleSetAlarm = () => {
    console.log("handleSetAlarm called")
    const now = new Date().getTime();
    const inFiveSeconds = now + 5000;
    setAlarm(inFiveSeconds);
  };

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Button title="Aseta TTS hälytys 5 sek kuluttua" onPress={handleSetAlarm} />
    </View>
  );
}
