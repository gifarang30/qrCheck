import React, {useEffect, useRef} from 'react';
import {View, ViewProps, StyleSheet, TextInput} from 'react-native';
import {useNavigation} from '@react-navigation/native';
import {Button, Input, Text} from 'react-native-elements';
import CarData from '../config/standard/data/CarData';
import * as AppDataUtil from '../assets/util/AppDataUtil';
import {isEmpty} from 'lodash';
import {jwtDecode, tcpSend} from '../assets/util/TcpUtil';
import * as Const from '../config/Const';
import Toast from 'react-native-toast-message';
import {getCameraPermission, getLocationPermission} from '../assets/util/PmsUtil';
import {emptyToZero, zeroToEmpty} from '../assets/util/StringUtil';
import {observer} from 'mobx-react';
import useStore from '../assets/stores/useStore';
import Tts from "react-native-tts";
import {JWT_SECRET_KEY, TTS_PITCH, TTS_RATE} from "../config/Const";
import {sign} from "react-native-pure-jwt";

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: "#fff",
        padding: 20,
        margin: 10,
    }
});

/**
 * 페이지 참조 클래스
 */
class PageRefs {
    tcpServerIp = useRef<TextInput>(null)
    tcpServerPort = useRef<TextInput>(null)
    carNum = useRef<TextInput>(null)
}

/**
 *
 * @param props RowProps
 * @constructor
 */
export const Main = observer((props: ViewProps) => {

    /**
     * 페이지 네비게이션 객체 생성
     */
    const navigation = useNavigation();

    /**
     * 페이지 참조 객체 생성
     */
    const pageRef = new PageRefs();

    /**
     * App 정보 저장소 참조
     */
    const appDataStore = useStore().appDataStore;

    /**
     * 초기 셋팅
     */
    useEffect(() => {

        /**
         * 카메라 및 GPS 정보 권한 요청
         */
        getCameraPermission().then(() => {
            getLocationPermission().then();
        });

        /**
         * APP 정보 셋팅
         */
        AppDataUtil.getAppData().then((extractAppData) => {
            appDataStore.setAppData(extractAppData);
        });

        /**
         * TTS 설정
         */
        Tts.setDefaultRate(TTS_RATE).then();
        Tts.setDefaultPitch(TTS_PITCH).then();

    }, []);

    /**
     * APP 정보 씽크
     */
    useEffect(() => {
        AppDataUtil.setAppData(appDataStore.appData).then();
    }, [appDataStore.appData]);

    /**
     * QR 스캐너 화면으로 이동
     */
    const goQrScan = async () => {

        if (isEmpty(appDataStore.appData.tcpServerIp)) {
            Toast.show({
                type: 'error',
                text1: '입력 경고',
                text2: '서버IP를 입력 해주세요.'
            });
            pageRef.tcpServerIp.current!!.focus()
            return;
        }

        if (appDataStore.appData.tcpServerPort == 0) {
            Toast.show({
                type: 'error',
                text1: '입력 경고',
                text2: '서버PORT를 입력 해주세요.'
            });
            pageRef.tcpServerPort.current!!.focus()
            return;
        }

        if (isEmpty(appDataStore.appData.carNum)) {
            Toast.show({
                type: 'error',
                text1: '입력 경고',
                text2: '차량번호를 입력 해주세요.'
            });
            pageRef.carNum.current!!.focus()
            return;
        }

        /**
         * 차랑 번호 붙여서 TCP 서버로 전송
         */
        tcpSend({
            actCd: '109', // 109 : 차량번호 확인
            carNum: appDataStore.appData.carNum

        /**
         * 응답 콜백
         */
        }, (resData) => {
            if (resData.resCd == Const.RES_CD_OK) {

                /**
                 * 차랑 정보
                 */
                const carData = resData.resVal as CarData;

                /**
                 * 차량 고유 시퀀스 추가 저장
                 */
                appDataStore.setAppData({
                    ...appDataStore.appData,
                    carSeq: carData.carSeq
                });

                Toast.show({
                    type: 'success',
                    text1: '성공',
                    text2: resData.resMsg
                });

                /**
                 * QR 스캐너 화면으로 이동
                 */
                setTimeout(() => {
                    // @ts-ignore
                    navigation.navigate({name: 'qrScan'});
                }, 500);

            } else {
                Toast.show({
                    type: 'error',
                    text1: '오류',
                    text2: resData.resMsg
                });
            }
        }).then();
    }

    /**
     * QR 문자열 테스트
     */
    const goQrTest = async () => {

        /**
         * 전문 내용은 JWT 형식을 따름
         */
        const testQr = await sign({
            'sub': 'kumo',
            'actCd': '101',
            'actVal': '226',
            'memberId': 'test123',
            'exp': 1651276799000,
        }, JWT_SECRET_KEY, {
            alg: 'HS256'
        });


        console.log(testQr);
        /**
         * QR 코드 JWT 문자열 디코드
         */
        jwtDecode(testQr).then((qrPayload) => {

            console.log(qrPayload)

            /**
             * 차랑 고유 시퀀스 붙여서 TCP 서버로 전송
             */
            tcpSend({
                carSeq: appDataStore.appData.carSeq,
                ...qrPayload

                /**
                 * 응답 콜백
                 */
            }, (resData) => {
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

            });
        });
    }

    return (

        <View style={styles.container}>

            <Input label='서버IP' placeholder='서버IP' autoCompleteType={String} value={appDataStore.appData.tcpServerIp}
                   ref={pageRef.tcpServerIp}
                   onChangeText={(changeValue) => {
                       appDataStore.setAppData({
                           ...appDataStore.appData,
                           tcpServerIp: changeValue
                       });
                   }}/>
            <Input label='서버PORT' placeholder='서버PORT' autoCompleteType={String} value={zeroToEmpty(appDataStore.appData.tcpServerPort)}
                   ref={pageRef.tcpServerPort} maxLength={6}
                   onChangeText={(changeValue) => {
                       appDataStore.setAppData({
                           ...appDataStore.appData,
                           tcpServerPort: emptyToZero(changeValue)
                       });
                   }}/>
            <Input label='차량번호' placeholder='차량번호' autoCompleteType={String} value={appDataStore.appData.carNum}
                   ref={pageRef.carNum}
                   onChangeText={(changeValue) => {
                       appDataStore.setAppData({
                           ...appDataStore.appData,
                           carNum: changeValue
                       });
                   }}/>

            <Button
                title={'확인'}
                containerStyle={{
                    marginHorizontal: 50,
                    marginVertical: 10,
                }}
                onPress={() => goQrScan()}
            />

            {/*<Button*/}
            {/*    title={'qr문자열 테스트'}*/}
            {/*    containerStyle={{*/}
            {/*        marginHorizontal: 50,*/}
            {/*        marginVertical: 10,*/}
            {/*    }}*/}
            {/*    onPress={() => goQrTest()}*/}
            {/*/>*/}
        </View>
    );
})

export default Main;