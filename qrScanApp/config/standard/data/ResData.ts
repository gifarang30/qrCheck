import {RES_CD_ERROR} from '../../Const';

/**
 * 응답 정보 클래스
 */
class ResData {

    /**
     * 응답 코드
     */
    public resCd: string = RES_CD_ERROR;

    /**
     * 응답 메시지
     */
    public resMsg: string = '알수 없는 오류가 발생했습니다.';

    /**
     * 응답 값
     */
    public resVal: {[key: string]: any} | null = null;
}

export default ResData;