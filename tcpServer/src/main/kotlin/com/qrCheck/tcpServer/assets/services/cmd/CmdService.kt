package com.qrCheck.tcpServer.assets.services.cmd

/**
 * OS 명령어 실행 인터페이스
 */
interface CmdService {

    /**
     * 명령어 실행
     * @param cmd - 명령어
     * @param cmdEncoding - 결과 인코딩
     * @return
     */
    @Throws(Exception::class)
    fun excuteCmd(cmd: String, cmdEncoding: String): String

    /**
     * 명령어 필터링 체크
     * @param cmdStr 명령어 문자열
     * @return 필터링 끝난 명령어 문자열
     */
    @Throws(Exception::class)
    fun chkCmdFilter(cmdStr: String): String

    /**
     * 지정된 명령어 인지 체크
     * @param cmd - 명령어
     * @return 명령어 문자열
     */
    @Throws(Exception::class)
    fun chkFixedCmd(cmd: String): String
}