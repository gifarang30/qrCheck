package com.qrCheck.tcpServer.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * 응답 데이터 프로퍼티
 * resources/spring/props/prop-res.yml 참조
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "prop-res")
data class PropRes(
    var resCd: Map<String, String> = mutableMapOf(),
    var resMsg: Map<String, String> = mutableMapOf()
)