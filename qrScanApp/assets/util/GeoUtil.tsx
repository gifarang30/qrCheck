import Geolocation from 'react-native-geolocation-service';
import {GEO_WATCH_DISTANCE_METER, GEO_WATCH_INTERVAL} from '../../config/Const';

/**
 * GPS 변경 감시 셋팅
 * @param rePosFn 위치 변경 콜백 함수
 * @return 위치 감시 객체 번호
 */
export const startWatchLoc = (rePosFn: (lat: number, lng: number) => void) => {
    return Geolocation.watchPosition(
        position => {
            const {latitude, longitude} = position.coords;
            rePosFn(latitude, longitude);
        },
        error => {
            console.log(error);
        },
        {
            enableHighAccuracy: true,
            accuracy: {
                android: 'high',
                ios: 'best',
            },
            distanceFilter: GEO_WATCH_DISTANCE_METER,
            useSignificantChanges: true,
            interval: GEO_WATCH_INTERVAL,
            fastestInterval: GEO_WATCH_INTERVAL,
        }
    );
}

/**
 * GPS 변경 감시 중단
 * @param watchId GPS 감시 객체 번호
 */
export const stopWatchLoc = (watchId: number) => {
    Geolocation.clearWatch(watchId);
}