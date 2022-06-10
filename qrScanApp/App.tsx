import React, {useEffect} from 'react';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import Main from './pages/Main';
import QrScan from './pages/QrScan';
import Toast from 'react-native-toast-message';
import {LogBox, StatusBar} from 'react-native';
import SplashScreen from 'react-native-splash-screen';
import {Provider} from 'mobx-react';
import RootStore from './assets/stores/RootStore';

/**
 * NativeEventEmitter 경고 픽스
 * 버전 충돌 문제..
 */
LogBox.ignoreLogs(['new NativeEventEmitter']); // Ignore log notification by message
LogBox.ignoreAllLogs(); //Ignore all log notifications

const Stack = createStackNavigator();

const App = (): JSX.Element => {

    /**
     * 로딩 화면 1.2초 후 제거
     */
    useEffect(() => {
        setTimeout(() => {
            SplashScreen.hide();
        }, 1200);
    }, []);

    return (
        <Provider {...RootStore()}>
            <NavigationContainer>
                <StatusBar barStyle="dark-content" />
                <Stack.Navigator initialRouteName="main">
                    <Stack.Screen name="main" component={Main} options={{ title: '금우 고속' }}/>
                    <Stack.Screen name="qrScan" component={QrScan} options={{ title: 'QR 스캐너' }}/>
                </Stack.Navigator>
                <Toast position="bottom" visibilityTime={2500} />
            </NavigationContainer>
        </Provider>
    );
};

export default App;
