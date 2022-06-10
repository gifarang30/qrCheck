import {isEmpty} from 'lodash';

/**
 * 영문 문자열 포함 여부
 * @param str 문자열
 * @return boolean
 */
export const hasEng = (str: string) => {
    if (isEmpty(str)) return false;
    return /[a-zA-Z]/.test(str);
}

/**
 * 영문 문자열 여부
 * @param str 문자열
 * @return boolean
 */
export const isEng = (str: string) => {
    if (isEmpty(str)) return false;
    return !/[^A-Za-z]/ig.test(str);
}

/**
 * 한글 문자열 포함 여부
 * @param str 문자열
 * @return boolean
 */
export const hasKor = (str: string) => {
    if (isEmpty(str)) return false;
    return /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/.test(str);
}

/**
 * 숫자 문자열 포함 여부
 * @param str 문자열
 * @return boolean
 */
export const hasNumber = (str: string) => {
    if (isEmpty(str)) return false;
    return /[0-9]/.test(str);
}

/**
 * 숫자 문자열 여부
 * @param str 문자열
 * @return boolean
 */
export const isNumber = (str: string) => {
    if (isEmpty(str)) return false;
    return !/[^0-9]/g.test(str);
}

/**
 * 특수 문자열 포함 여부
 * @param str 문자열
 * @return boolean
 */
export const hasSpecial = (str: string) => {
    if (isEmpty(str)) return false;
    return /[~!@#$%^&*()_+|<>?:{}]/.test(str);
}

/**
 * 코드로 사용 가능한 문자열 여부
 * 영문으로 시작하고 숫자가 포함될수 있다.
 * @param str 문자열
 * @return boolean
 */
export const isCode = (str: string) => {
    if (isEmpty(str)) return false;
    if (/[^A-Za-z]/ig.test(str.substring(0, 1))) return false;
    if (/[^A-Za-z0-9]/ig.test(str)) return false;
    return true;
}

/**
 * 문자열 길이 체크
 * @param str 문자열
 * @param minLen 최소 길이
 * @param maxLen 최대 길이
 * @return boolean
 */
export const chkStrLen = (str: string, minLen: number, maxLen: number = 99999) => {
    if (isEmpty(str)) return false;
    return str.length >= minLen && str.length <= maxLen
}

/**
 * 좌측에 특정문자 삽입
 * @param str 대상 문자열 및 숫자
 * @param len 채울 자릿수
 * @param addStr 삽입 문자열
 */
export const strPadLeft = (str: string | number, len = 2, addStr = '0') => {

    let resStr = '';

    // 숫자면 문자로 캐스팅
    if (typeof str == 'number') {
        str = str.toString();
    }

    for (let i = len - str.length; i > 0; i--) {
        resStr += addStr;
    }
    resStr += str;

    return resStr;
}

/**
 * 문자열 바이트 수
 * @param str 문자열
 */
export const getByteLen = (str: string) => {
    return str
        .split('')
        .map(s => s.charCodeAt(0))
        .reduce((prev, c) => (prev + ((c === 10) ? 2 : ((c >> 7) ? 2 : 1))), 0);
}

/**
 * 0 숫자를 빈 문자열로 변환
 * @param num 숫자
 * @return 문자열
 */
export const zeroToEmpty = (num: number) => {
    return num == 0 ? '' : num.toString();
}

/**
 * 빈 문자열을 숫자 0 으로 변환
 * @param str 문자열
 * @return 숫자
 */
export const emptyToZero = (str: string) => {
    return isEmpty(str) ? 0 : Number(str);
}