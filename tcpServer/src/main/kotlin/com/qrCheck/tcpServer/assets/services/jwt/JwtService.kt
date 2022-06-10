package com.qrCheck.tcpServer.assets.services.jwt

import io.netty.buffer.ByteBuf

/**
 * JWT 서비스 인터페이스
 */
interface JwtService {

    /**
     * JWT 인코딩
     * @param subject JWT Subject 핸들러 분기 코드로 활용
     * @param jwtDataMap JWT 데이터 맵
     * @param expiredTime 인증 만료 시간
     * @return String JWT 문자열
     */
    @Throws(Exception::class)
    fun encodeJwt(subject: String, jwtDataMap: MutableMap<String, Any>, expiredTime: Long = 60000L): String

    /**
     * JWT 디코딩
     * @param inMsgBuf 수신 전문 ByteBuf
     * @return 파싱된 맵 정보
     */
    @Throws(Exception::class)
    fun decodeJwt(inMsgBuf: ByteBuf): MutableMap<String, Any>
}