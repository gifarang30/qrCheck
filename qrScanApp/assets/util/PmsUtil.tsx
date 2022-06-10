import {PermissionsAndroid} from 'react-native';

/**
 * 카메라 접근 권한 요청
 */
export const getCameraPermission = async () => {

    try {
        const granted = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.CAMERA,
            {
                title: "Cool Photo App Camera Permission",
                message: "Cool Photo App needs access to your camera so you can take awesome pictures.",
                buttonNeutral: "Ask Me Later",
                buttonNegative: "Cancel",
                buttonPositive: "OK"
            }
        );
        if (granted === PermissionsAndroid.RESULTS.GRANTED) {
            console.log("You can use the CAMERA");
        } else {
            console.log("CAMERA permission denied");
        }
    } catch (err) {
        console.warn(err);
    }
}

/**
 * GPS 정보 접근 권한 요청
 */
export const getLocationPermission = async () => {

    try {
        const granted01 = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
            {
                title: "Cool Location Info Permission",
                message: "Cool Location Info service access to your GPS INFO so you can take awesome service.",
                buttonNeutral: "Ask Me Later",
                buttonNegative: "Cancel",
                buttonPositive: "OK"
            }
        );
        if (granted01 === PermissionsAndroid.RESULTS.GRANTED) {
            console.log("You can use the ACCESS_FINE_LOCATION");
        } else {
            console.log("your ACCESS_FINE_LOCATION access permission denied");
        }
        const granted02 = await PermissionsAndroid.request(
            PermissionsAndroid.PERMISSIONS.ACCESS_COARSE_LOCATION,
            {
                title: "Cool Location COARSE Permission",
                message: "Cool Location COARSE service access to your GPS INFO so you can take awesome service.",
                buttonNeutral: "Ask Me Later",
                buttonNegative: "Cancel",
                buttonPositive: "OK"
            }
        );
        if (granted02 === PermissionsAndroid.RESULTS.GRANTED) {
            console.log("You can use the ACCESS_COARSE_LOCATION");
        } else {
            console.log("your ACCESS_COARSE_LOCATION access permission denied");
        }
    } catch (err) {
        console.warn(err);
    }
}
