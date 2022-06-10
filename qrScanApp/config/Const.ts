import Tts from "react-native-tts";

/**
 * TCP 서버 설정
 */
export const TCP_SERVER_IP = '203.251.57.109';                                          // TCP 서버 아이피(앱 메인에서 수정가능)
export const TCP_SERVER_PORT = 29001;                                                   // TCP 서버 포트(앱 메인에서 수정가능)
export const TCP_MSG_TOTAL_BYTE_LEN = 4;                                                // TCP 전문 전체 길이 정보 byte 길이

/**
 * JWT 설정
 */
export const JWT_SUBJECT = 'kumo';                                                      // JWT 주제
export const JWT_SECRET_KEY = 'andzeXM=';                                               // JWT 비밀키

/**
 * 스토리지의 APP 정보 키
 */
export const APP_DATA_KEY = 'appData';

/**
 * GPS 추적 관련 설정
 * 오차 범위 40미터 이상임
 */
export const GEO_WATCH_DISTANCE_METER = 1;                                              // GPS 추적 감시 최소 거리 미터
export const GEO_WATCH_INTERVAL = 1000;                                                 // GPS 추적 간격 밀리 초 단위

/**
 * TTS 설정
 */
export const TTS_RATE = 0.55;                                                           // TTS 속도(0.01 ~ 0.99)
export const TTS_PITCH = 1;                                                             // TTS 소리 크기(0.5 ~ 2.0)

/**
 * QR 스캔 관련 설정
 */
export const QR_SCAN_REACTIVE_TIMEOUT = 1500;                                           // QR 스캔 재동작 중지 밀리 초
export const QR_SCAN_REACTIVE_MAX_TIMEOUT = 4000;                                       // QR 스캔 최대 재동작 중지 밀리 초

/**
 * 코드 분리 문자열
 */
export const CODE_DIV = '|';

/**
 * GET 파라미터 코드 분리 문자열
 */
export const PARAM_CODE_DIV = '_';

/**
 * true, false 문자열
 */
export const ATT_STR_TRUE = 'true';
export const ATT_STR_FALSE = 'false';

/**
 * Axios 설정
 */
export const AXIOS_WITH_CREDENTIALS = true;                                             // 타 도메인 쿠키전송 가능 설정
export const AXIOS_TIMEOUT = 30000;                                                     // 타임아웃 30초
export const AXIOS_ACCESS_TOKEN_KEY = 'Authorization';                                  // 인증 토큰 키
export const AXIOS_GRANT_TYPE = 'Bearer';                                               // 인증 타입
export const AXIOS_REISSUE_TOKEN_STATUS = 401;                                          // 재인증 필요한 https status

/**
 * 응답 코드
 */
export const RES_CD_OK = '100';                                                         // 정상
export const RES_CD_JWT_DECODE_ERROR = '701';                                           // JWT 디코드 오류
export const RES_CD_VALID = '801';                                                      // 시스템 체크에 걸림
export const RES_CD_QR_ERROR = '811';                                                   // QR 코드 오류
export const RES_CD_ERROR = '999';                                                      // 오류

/**
 * 메시지 상수
 */
export const MSG_Y = 'Y';                                                               // 메시지 Y
export const MSG_N = 'N';                                                               // 메시지 N

/**
 * Form 관련 상수
 */
export const FORM_EMPTY = '';                                                           // 폼 빈 값
export const FORM_CHECK_Y = 'Y';                                                        // 폼 체크 됨 값
export const FORM_CHECK_N = 'N';                                                        // 폼 체크 안함 값