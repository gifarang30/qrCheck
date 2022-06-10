package com.qrCheck.tcpServer

import com.qrCheck.tcpServer.config.props.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(
    PropCmm::class, PropCrypt::class, PropRes::class,
    PropsDatasource::class, PropCmd::class)
class TcpServerApplication

fun main(args: Array<String>) {
    runApplication<TcpServerApplication>(*args)
}