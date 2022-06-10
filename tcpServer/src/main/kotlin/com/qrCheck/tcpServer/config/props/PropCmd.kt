package com.qrCheck.tcpServer.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * OS 명령어 관련 프로퍼티
 * resources/spring/props/prop-cmd.yml 참조
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "prop-cmd")
data class PropCmd(
    var cmdFilterList: Set<String> = mutableSetOf(),
    var cmdFixedList: Set<String> = mutableSetOf()
)