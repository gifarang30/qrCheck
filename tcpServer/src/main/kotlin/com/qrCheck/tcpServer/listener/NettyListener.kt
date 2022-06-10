package com.qrCheck.tcpServer.listener

import com.qrCheck.tcpServer.assets.services.netty.NettyService
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import javax.annotation.Resource

@Component
class NettyListener(

    @Resource(name="nettyServiceImpl")
    private val nettyService: NettyService

) : ApplicationListener<ApplicationReadyEvent> {

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        nettyService.start()
    }
}