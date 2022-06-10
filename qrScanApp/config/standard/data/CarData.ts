import {TCP_SERVER_HOST, TCP_SERVER_PORT} from '../../Const';
import AppData from "./AppData";

/**
 * 차량 정보 클래스
 */
class CarData {

    /**
     * 차량 고유 시퀀스
     */
    public carSeq: number = 0;

    /**
     * 차량 번호
     */
    public carNum: string = '';

    /**
     * 차량 직원 명
     */
    public crewName: string = '';

    /**
     * 차량 직원 연락처
     */
    public crewTel: string = '';
}

export default CarData;