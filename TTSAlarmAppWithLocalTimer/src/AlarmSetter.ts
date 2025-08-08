import { NativeModules, Platform } from 'react-native';

export function setAlarm(timestamp: number) {
  if (Platform.OS === 'android') {
    NativeModules.AlarmModule.setAlarm(timestamp);
  }
}
