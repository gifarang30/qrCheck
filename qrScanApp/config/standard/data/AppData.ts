import {TCP_SERVER_IP, TCP_SERVER_PORT} from '../../Const';

/**
 * App 정보 클래스
 */
class AppData {

    /**
     * TCP 서버 아이피
     */
    public tcpServerIp: string = TCP_SERVER_IP;

    /**
     * TCP 서버 포트
     */
    public tcpServerPort: number = TCP_SERVER_PORT;

    /**
     * 차량 번호
     */
    public carNum: string = '전남71자5023';

    /**
     * 차량 고유 시퀀스
     */
    public carSeq: number = 0;

    /**
     * GPS 감시 여부
     */
    public isWatch: boolean = true;

    /**
     * GPS 감시 객체 번호
     */
    public watchId: number =  0;
}

export default AppData;