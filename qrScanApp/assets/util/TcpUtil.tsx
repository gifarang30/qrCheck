import TcpSocket from 'react-native-tcp-socket';
import {sign, decode} from 'react-native-pure-jwt';
import {getByteLen, strPadLeft} from './StringUtil'
import {JsonType} from '../../config/standard/types/BaseTypes';
import ResData from '../../config/standard/data/ResData';
import * as AppDataUtil from '../../assets/util/AppDataUtil';
import {
    JWT_SECRET_KEY, JWT_SUBJECT, RES_CD_JWT_DECODE_ERROR, TCP_MSG_TOTAL_BYTE_LEN
} from '../../config/Const';
import Toast from 'react-native-toast-message';
import exp from "constants";

/**
 * JWT 디코드
 * @param str JWT 문자열
 */
export const jwtDecode = async (str: string): Promise<JsonType> => {
    let jwtPayload = {}
    try {
        const decodeRes = await decode(str, JWT_SECRET_KEY);
        jwtPayload = decodeRes.payload;
    } catch (e) {}
    return jwtPayload;
}

/**
 * JWT 디코드
 * @param str JWT 문자열
 */
export const jwtDecodeToResData = async (str: string) => {
    let resData = new ResData()
    try {
        const decodeRes = await decode(str, JWT_SECRET_KEY);
        resData = decodeRes.payload as ResData
    } catch (e) {
        console.log(e);
        resData.resCd = RES_CD_JWT_DECODE_ERROR;
        resData.resMsg = 'JWT 디코드 에러 발생';
    }
    return resData;
}

/**
 * JWT 만료 시간 값 생성
 * 유효 시간 10분
 */
export const getJwtExp = () => {
    return (new Date()).getTime() + 600000
}

/**
 * TCP 소켓 전송
 * @param jsonPayload JSON 페이로드
 * @param successFn 성공 콜백
 * @param errorFn 에러 콜백
 */
export const tcpSend = async (jsonPayload: JsonType,
    successFn: (resData: ResData) => void, errorFn?: (error: Error) => void) => {

    /**
     * APP 정보 추출
     */
    const appData = await AppDataUtil.getAppData();

    /**
     * TCP 소켓 생성
     */
    const socket = TcpSocket.createConnection({
        host: appData.tcpServerIp,
        port: appData.tcpServerPort,
        timeout: 5000,
    }, async () => {

        /**
         * JWT 토큰 만료시간(exp)이 지정되어 있을 경우
         * 신규로 만료시간을 선언하지 않음.
         */
        let payload: JsonType = {}
        if (jsonPayload.hasOwnProperty('exp')) {

            /**
             * 만료일 10자리 >> 13자리로 보정
             */
            let expStr = jsonPayload.exp!!.toString();
            if (expStr.length == 10) {
                expStr += '000';
                jsonPayload.exp = Number(expStr);
            }

            payload = {
                ...jsonPayload,
                sub: JWT_SUBJECT
            }

        } else {
            payload = {
                ...jsonPayload,
                exp: getJwtExp(),
                sub: JWT_SUBJECT
            }
        }

        /**
         * 전문 내용은 JWT 형식을 따름
         */
        const msgBody = await sign(payload, JWT_SECRET_KEY, {
            alg: 'HS256'
        });

        //console.log(jwtDecodeToResData(msgBody))

        /**
         * 전문 전체 바이트 수 정보를 헤더에 기록
         */
        const sendMsg = `${strPadLeft(getByteLen(msgBody), TCP_MSG_TOTAL_BYTE_LEN)}${msgBody}`;

        /**
         * 전문 발송
         */
        socket.write(sendMsg);

        /**
         * TCP 연결 해제
         */
        //socket.destroy();
    });

    /**
     * 응답 이벤트 처리
     */
    socket.on('data', (data) => {

        /**
         * JWT 디코드 후 성공 콜백으로 전달
         */
        jwtDecodeToResData(data.toString()).then((resData) => {
            successFn(resData);
        });

        /**
         * TCP 연결 해제
         */
        socket.destroy();
    });

    /**
     * 에러 이벤트 처리
     */
    socket.on('error', (error) => {

        let errorMsg = '알수 없는 에러 발생';
        try {
            errorMsg = error as unknown as string;
        } catch (e) {}

        /**
         * 에러 메시지 치환
         */
        switch (errorMsg) {
            case 'Host unreachable':
                errorMsg = '서버 연결이 되지 않습니다.';
                break;
        }

        Toast.show({
            type: 'error',
            text1: '에러 발생',
            text2: errorMsg
        });

        if (errorFn) {
            errorFn(error);
        }

        /**
         * TCP 연결 해제
         */
        socket.destroy();
    });

    /**
     * TCP 연결 해제 이벤트 처리
     */
    socket.on('close',  () => {

        /**
         * TCP 연결 해제
         */
        socket.destroy();
    });

    return socket;
}