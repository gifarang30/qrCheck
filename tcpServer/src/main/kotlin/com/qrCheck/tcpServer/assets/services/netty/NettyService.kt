package com.qrCheck.tcpServer.assets.services.netty

/**
 * Netty 서버 서비스 인터페이스
 */
interface NettyService {

    /**
     * Netty 서버 시작
     */
    @Throws(Exception::class)
    fun start()

    /**
     * Netty 서버 종료
     */
    @Throws(Exception::class)
    fun stop()
}