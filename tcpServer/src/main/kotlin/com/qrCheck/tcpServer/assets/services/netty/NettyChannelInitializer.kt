package com.qrCheck.tcpServer.assets.services.netty

import com.qrCheck.tcpServer.assets.services.crypt.CryptService
import com.qrCheck.tcpServer.assets.services.jwt.JwtService
import com.qrCheck.tcpServer.assets.services.kumo.KumoService
import com.qrCheck.tcpServer.config.props.PropCmm
import com.qrCheck.tcpServer.config.props.PropRes
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.annotation.Resource

/**
 * 채널 생성 구현부
 * 최초 커넥션 발생할때 마다 채널 생성 됨
 */
@Component
class NettyChannelInitializer(

    private val propCmm: PropCmm,
    private val propRes: PropRes,

    /**
     * JWT 관련 서비스
     */
    @Resource(name = "jwtServiceImpl")
    private val jwtService: JwtService,

    /**
     * 암복호화 관련 서비스
     */
    @Resource(name = "cryptServiceImpl")
    private val cryptService: CryptService,

    /**
     * 금우 관련 서비스
     */
    @Resource(name = "kumoServiceImpl")
    private val kumoService: KumoService

) : ChannelInitializer<SocketChannel>() {

    /**
     * 로거
     */
    private val logger: Logger = LoggerFactory.getLogger(NettyChannelInitializer::class.java)

    /**
     * 채널이 생성될 때 호출
     */
    override fun initChannel(ch: SocketChannel) {

        logger.debug("00-01. 채널 생성 시작")

        /**
         * 채널 파이프 라인
         */
        val pipeline: ChannelPipeline = ch.pipeline()

        /**
         * 디코더 추가
         * 디코더는 Bean 객체 주입이 안되므로 매번 새로운 객체를 생성 해야 함
         * 수신 전문이 전체 바이트에 도달 전까지 스킵 및 변환 역활
         * 최대 수신 전문 바이트 초과시 이상 요청으로 간주하고 채널 닫음
         */
        val nettyDecoder = NettyDecoder(propCmm, propRes, jwtService)
        pipeline.addLast(nettyDecoder)
        logger.debug("00-02. 채널 디코더[nettyDecoder] 셋팅")

        /**
         * 금우 수신 핸들러 추가
         * 객체 shared 관련 문제로 객체 생성해서 사용 함
         */
        val nettyHandler = NettyHandler(propCmm, propRes, cryptService, jwtService, kumoService)
        pipeline.addLast(nettyHandler)
        logger.debug("00-03. 채널 핸들러[nettyHandler] 셋팅")

        logger.debug("00-04. 채널 생성 완료")
    }
}