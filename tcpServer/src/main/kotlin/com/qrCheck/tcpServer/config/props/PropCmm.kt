package com.qrCheck.tcpServer.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * 공통 설정 프로퍼티
 * resources/spring/props/prop-cmm.yml 참조
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "prop-cmm")
data class PropCmm(
    var tcpBossCnt: Int = 0,
    var tcpWorkerCnt: Int = 0,
    var tcpConSocCnt: Int = 0,
    var tcpServerHost: String = "",
    var tcpServerPort: Int = 0,
    var tcpBuffSize: Int = 0,
    var msgTotalByteLen: Int = 0,
    var msgCharset: String = ""
)
