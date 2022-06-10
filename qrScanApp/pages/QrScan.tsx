import React, {useEffect, useState} from 'react';
import {View, ViewProps} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import { Dimensions } from 'react-native';
import QRCodeScanner from 'react-native-qrcode-scanner';
import {BarCodeReadEvent, RNCameraProps} from 'react-native-camera';
import {jwtDecode, tcpSend} from '../assets/util/TcpUtil';
import Tts from 'react-native-tts';
import * as Const from '../config/Const';
import Toast from 'react-native-toast-message';
import * as AppDataUtil from '../assets/util/AppDataUtil';
import {startWatchLoc, stopWatchLoc} from '../assets/util/GeoUtil';
import {Switch, Text} from 'react-native-elements';
import {getLocationPermission} from '../assets/util/PmsUtil';
import {
    JWT_SUBJECT,
    QR_SCAN_REACTIVE_MAX_TIMEOUT,
    QR_SCAN_REACTIVE_TIMEOUT,
    TTS_PITCH,
    TTS_RATE
} from '../config/Const';
import {observer} from 'mobx-react';
import useStore from '../assets/stores/useStore';
import {JsonType} from "../config/standard/types/BaseTypes";

/**
 * 디바이스 넓이와 높이 값 추출
 */
const deviceWidth = Dimensions.get('screen').width;
const deviceHeight = Dimensions.get('screen').height;

/**
 *
 * @param props RowProps
 * @constructor
 */
const QrScan = observer((props: ViewProps) => {

    /**
     * QR 스캔 동작 플래그
     */
    const [qrScanFlag, setQrScanFlag] = useState(true);

    /**
     * GPS 정보
     */
    const [gps, setGps] = useState({lat: 0, lng: 0});

    /**
     * 카메라 프로퍼티
     */
    const [cameraProps, setCameraProps] = useState<RNCameraProps>({
        type: 'front'
    })

    /**
     * App 정보 저장소 참조
     */
    const appDataStore = useStore().appDataStore;

    /**
     * 네비게이터
     */
    const navigation = useNavigation();

    /**
     * 스캐너
     */
    const scanner = React.useRef('');

    /**
     * 초기 셋팅
     */
    useEffect(() => {

        /**
         * APP 정보 추출
         */
        AppDataUtil.getAppData().then((extractAppData) => {

            /**
             * APP 정보 저장소에 저장
             */
            appDataStore.setAppData(extractAppData);

            /**
             * 위치 정보 권한 획득 후 GPS 기능 사용
             */
            getLocationPermission().then(() => {
                setWatchLoc(extractAppData.isWatch);
            });
        });

        /**
         * TTS 설정
         */
        Tts.setDefaultRate(TTS_RATE).then();
        Tts.setDefaultPitch(TTS_PITCH).then();

    }, []);

    /**
     * GPS 감시 셋팅
     * @param _isWatch GPS 감시 사용 여부
     * @return 위치 감시 객체 번호
     */
    const setWatchLoc = (_isWatch: boolean) => {
        let _watchId = 0;
        if (_isWatch) {
            _watchId = startWatchLoc(reposFn);
            console.log(`GPS 감시 시작 : ${_watchId}`);
        } else {
            stopWatchLoc(appDataStore.appData.watchId);
            console.log(`GPS 감시 종료 : ${appDataStore.appData.watchId}`);
        }
        appDataStore.setAppData({
            ...appDataStore.appData,
            isWatch: _isWatch,
            watchId: _watchId
        });
        return _watchId;
    }

    /**
     * GPS 감시 콜백 함수
     * @param lat GPS LAT
     * @param lng GPS LNG
     */
    const reposFn = (lat: number, lng: number) => {

        setGps({
            ...gps,
            lat: lat,
            lng: lng
        });

        /**
         * 차랑 고유 시퀀스 붙여서 TCP 서버로 전송
         */
        tcpSend({
            carSeq: appDataStore.appData.carSeq,
            actCd: '201',
            carLat: lat.toString(),
            carLng: lng.toString()

        /**
         * 응답 콜백
         */
        }, (resData) => {
            if (resData.resCd == Const.RES_CD_OK) {
                console.log(resData.resMsg);
            } else {
                console.log(resData.resMsg);
            }
        }).then();
    }

    /**
     * QR 스캔 성공 콜백 함수
     * @param qrScanEvent QR 스캔 이벤트
     */
    const onQrScanSuccess = (qrScanEvent: BarCodeReadEvent) => {

        /**
         * QR 코드 JWT 문자열 디코드
         */
        jwtDecode(qrScanEvent.data).then((qrPayload) => {

            /**
             * QR 스캔 재동작 중지
             */
            setQrScanFlag(false);


            const payload: JsonType = {
                ...qrPayload,
                carSeq: appDataStore.appData.carSeq
            }

            /**
             * 차랑 고유 시퀀스 붙여서 TCP 서버로 전송
             */
            tcpSend(payload, (resData) => {
                if (resData.resCd == Const.RES_CD_OK) {
                    Tts.speak(resData.resMsg);
                    Toast.show({
                        type: 'success',
                        text1: '성공',
                        text2: resData.resMsg
                    });
                } else {
                    Tts.speak(resData.resMsg);
                    Toast.show({
                        type: 'error',
                        text1: '에러 발생',
                        text2: `시스템 에러 : ${resData.resMsg}`
                    });
                }
            }, (error) => {
                Tts.speak(`예약 확인 안됨`);
                Toast.show({
                    type: 'error',
                    text1: '에러 발생',
                    text2: `시스템 에러 : ${error.message}`
                });
            }).then(() => {

                /**
                 * QR 스캔 재동작 시작
                 */
                setQrScanFlag(true);
            });

            /**
             * QR 스캔 특정 시간 이후 재동작 시작
             */
            setTimeout(() => {
                if (!qrScanFlag) {
                    setQrScanFlag(true);
                }
            }, QR_SCAN_REACTIVE_MAX_TIMEOUT);
        });
    }

    return (
        <View>
            <View style={{
                backgroundColor : '#ececec',
                flexDirection: 'row'
            }}>
                <Text style={{lineHeight: 30, marginLeft:5}}>카메라 :</Text>
                <Switch
                    value={appDataStore.appData.isWatch}
                    style={{marginLeft: 0, marginRight: 10, height: 30}}
                    onValueChange={(value) => {
                        setWatchLoc(value);
                    }}
                />
                <Text style={{lineHeight: 30, marginLeft:5}}>GPS 사용 :</Text>
                <Switch
                    value={appDataStore.appData.isWatch}
                    style={{marginLeft: 0, marginRight: 10, height: 30}}
                    onValueChange={(value) => {
                        setWatchLoc(value);
                    }}
                />
                <Text style={{lineHeight: 30}}>[ {gps.lat}, {gps.lng} ]</Text>
            </View>

            <QRCodeScanner
                cameraProps={cameraProps}
                containerStyle={{marginTop: 0, height: deviceHeight}}
                cameraStyle={[{marginTop: 0, height:deviceHeight - 157}]}
                reactivate={qrScanFlag}
                reactivateTimeout={QR_SCAN_REACTIVE_TIMEOUT}
                vibrate={false}
                showMarker={true}
                ref={(node) => {
                    if (scanner) { // @ts-ignore
                        scanner.current = node
                    }
                }}
                onRead={onQrScanSuccess}
            />
        </View>
    );
});

export default QrScan;