# react-native-examples
This repo contains all kind of useful tips for a react-development 

# Setting up development environment

## Setup Expo environment

1. Install latest node.js LTS version.

2. Follow instructions of this web page (Expo Go Quickstart):

https://reactnative.dev/docs/environment-setup?guide=quickstart

3. Run **expo** command to see that expo has been installed successfully.

## Running Expo app on the device

1. Execute **npm start** command on the main folder of the project.
2. Execute **npm run android** command if you want execute app on the android emulator.

## Running react-native-course Expo app on the emulator

1. Create folder xxx
2. Note: this step is not needed unless code is done from scratch. Execute **npx create-expo-app@latest** command on xxx folder.
3. Copy react-native-course test code to the xxx folder exclusing package.json.
4. Update xxx/package.json to meet react-native-course example code.
5. Execute **npm install** command.
6. Run **npm start** command. 

# Tips and tricks

## Updating state
This is a correct way to add object to the list in react app.

```
export default function App(){
  const [courseGoals,setCourseGoals] = useState([])
  ...

  function addGoalHandler(){
    setCourseGoals(currentCourseGoals => [...currentCourseGoals, enteredGoalText]);
  }
}
```

## Architecture of my alarm app
This chapter evaluates different architecture alternatives to create my voice alarm app.

Expo cannot be used bacause it is not possible to launch expo application by "alarm clock" style without any user's interaction.  

### Expo
This chapter contains expo technologies to implement my alarm application. It seems that it is not possible to launch expo application by "alarm clock" style because expo does not support "headless background services" functionalities.

#### Send notifications from the server to the app
expo-notifications package contains functionality to send notifications to the app. "Headless Background Notification" mode executes js code without given any notificattion to the user.
Links:
https://docs.expo.dev/versions/latest/sdk/notifications/
https://docs.expo.dev/push-notifications/what-you-need-to-know/
https://docs.expo.dev/push-notifications/what-you-need-to-know/#headless-background-notifications

### React native
In the react native application is possible to receive push notifications from the server and execute text-to-speech functionality in the headless mode.

Idea of the application is that server sends push notification to the app every time when timer expires. So android app is just headless app which functionality is repeat text as a voice message.
