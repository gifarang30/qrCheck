package com.qrCheck.tcpServer.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * 암복호화 관련 설정 프로퍼티
 * resources/spring/props/prop-crypt.yml 참조
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "prop-crypt")
data class PropCrypt(
    var jwtSecretKey: String = "",
    var cryptIterationCount: Int = 0,
    var cryptKeyLength: Int = 0,
    var cryptAlgorithm: String = "",
    var cryptPassword: String = "",
    var cryptSalt: String = "",
    var cryptIv: String = ""
)
