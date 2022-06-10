package com.qrCheck.tcpServer.assets.services.netty

import com.qrCheck.tcpServer.config.props.PropCmm
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import org.springframework.stereotype.Service
import java.net.InetSocketAddress
import javax.annotation.PreDestroy

/**
 * Netty 서버 서비스 구현부
 */
@Service("nettyServiceImpl")
class NettyServiceImpl(

    private val propCmm: PropCmm,

    private val nettyChannelInitializer: NettyChannelInitializer

    ): NettyService {

    private var serverChannel: Channel? = null

    /**
     * Netty 서버 시작
     */
    @Throws(Exception::class)
    override fun start() {

        /**
         * boss 및 worker nio 그룹
         */
        val bossGroup: EventLoopGroup = NioEventLoopGroup(propCmm.tcpBossCnt)
        val workerGroup: EventLoopGroup = NioEventLoopGroup(propCmm.tcpWorkerCnt)

        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java)
                .option(ChannelOption.SO_BACKLOG, propCmm.tcpConSocCnt)
                .option(ChannelOption.SO_RCVBUF, propCmm.tcpBuffSize)
                .childOption(ChannelOption.SO_RCVBUF, propCmm.tcpBuffSize)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(nettyChannelInitializer)

            /**
             * 서버 채널 셋팅
             */
            serverChannel = b.bind(InetSocketAddress(propCmm.tcpServerHost, propCmm.tcpServerPort))
                .sync().channel()
            serverChannel!!.closeFuture().sync()

        } finally {
            workerGroup.shutdownGracefully()
            bossGroup.shutdownGracefully()
        }
    }

    /**
     * Netty 서버 종료
     */
    @PreDestroy
    @Throws(Exception::class)
    override fun stop() {
        if (serverChannel != null) {
            serverChannel!!.close()
            serverChannel!!.parent().close()
        }
    }

}