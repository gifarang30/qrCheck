import {APP_DATA_KEY} from '../../config/Const';
import AppData from '../../config/standard/data/AppData';
import AsyncStorage from "@react-native-async-storage/async-storage";

/**
 * APP 정보 저장
 * @param appData APP 정보
 */
export const setAppData = async (appData: AppData) => {
    await AsyncStorage.setItem(APP_DATA_KEY, JSON.stringify(appData));
}

/**
 * APP 정보 추출
 * @return APP 정보
 */
export const getAppData = async (): Promise<AppData> => {
    const appDataStr = await AsyncStorage.getItem(APP_DATA_KEY);
    let appData = new AppData();
    if (appDataStr != null) {
        appData = {
            ...appData,
            ...(JSON.parse(appDataStr) as AppData)
        };
    }
    return appData;
}