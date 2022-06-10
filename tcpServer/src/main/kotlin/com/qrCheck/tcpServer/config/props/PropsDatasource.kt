package com.qrCheck.tcpServer.config.props

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * 데이터소스 프로퍼티
 * resources/spring/props/prop-datasource.yml 참조
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "datasource")
data class PropsDatasource(
    var kumo: Map<String, String> = mutableMapOf()
)