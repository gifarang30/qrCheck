package com.qrCheck.tcpServer.assets.services.crypt

/**
 * 암복호화 서비스
 */
interface CryptService {

    /**
     * 복호화 함수
     * @param str 복호화 대상 문자열
     * @return 복호화 된 문자열
     */
    @Throws(Exception::class)
    fun decrypt(str: String): String

    /**
     * 암호화 함수
     * @param str 암호화 대상 문자열
     * @return 암호화 된 문자열
     */
    @Throws(Exception::class)
    fun encrypt(str: String): String
}