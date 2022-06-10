package com.qrCheck.tcpServer.assets.services.netty

import com.qrCheck.tcpServer.assets.services.jwt.JwtService
import com.qrCheck.tcpServer.config.props.PropCmm
import com.qrCheck.tcpServer.config.props.PropRes
import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ByteToMessageDecoder
import io.netty.handler.codec.CorruptedFrameException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

/**
 * Netty 디코더
 * 디코더는 Bean 객체 주입이 안되므로 매번 새로운 객체를 생성 해야 함
 * 수신 전문이 전체 바이트에 도달 전까지 스킵 및 변환 역활
 * 최대 수신 전문 바이트 초과시 이상 요청으로 간주하고 채널 닫음
 */
class NettyDecoder(
    private val propCmm: PropCmm,
    private val propRes: PropRes,
    private val jwtService: JwtService
) : ByteToMessageDecoder() {

    /**
     * 전문 전체 바이트 길이
     */
    private var msgTotalByte = 0


    /**
     * 전문 수신 완료 플래그
     */
    private var msgCompleted = false

    /**
     * 로거
     */
    private val logger: Logger = LoggerFactory.getLogger(NettyDecoder::class.java)

    @Throws(NettyDecoderException::class)
    override fun decode(ctx: ChannelHandlerContext, inMsgBuf: ByteBuf, outMsgList: MutableList<Any>) {

        /**
         * 전문 전체 바이트 길이 0 이면 초기 수신
         */
        if (msgTotalByte == 0) {

            /**
             *  최초 전문의 수신 바이트 길이가 '전문 전체 바이트 길이' 정보 길이보다 적으면 오류
             */
            if (inMsgBuf.readableBytes() < propCmm.msgTotalByteLen) {
                throw NettyDecoderException("전문 전체 바이트 길이를 알수 없습니다.")
            }


            // 수신 메시지 버퍼 전체 바이트 수 추출(초기 특정 자리수는 전체 바이트 길이)
            msgTotalByte = inMsgBuf.readBytes(propCmm.msgTotalByteLen).toString(Charset.forName(propCmm.msgCharset)).toInt()

            logger.debug("01-01. 초기 수신 발생")
        }

        /**
         * 전문 수신 완료 처리 된 경우 디코딩 처리 중지
         * 스트림 특성상 여러번 처리 할려고 함
         */
        if (msgCompleted) {
            return
        }

        /**
         * 수신 전문이 전체 바이트 길이에 도달 했는지 체크
         */
        logger.debug("01-02. 수신 ${inMsgBuf.readableBytes()}/$msgTotalByte byte")
        if (this.msgTotalByte > inMsgBuf.readableBytes()) {
            return
        }

        /**
         * 채널 버퍼 사이즈 이상이면 채널 닫기
         */
        if (inMsgBuf.readableBytes() > propCmm.tcpBuffSize) {
            throw NettyDecoderException("수신 된 전문 길이가 허용 된 범위를 초과했습니다.")
        }

        /**
         * 수신 메시지 파싱
         */
        logger.debug("02-01. 수신 전문 파싱 시작")

        /**
         * 디코딩 맵
         */
        val decodeMap: MutableMap<String, Any> = jwtService.decodeJwt(inMsgBuf)
        logger.debug(decodeMap.toString())
        logger.debug("02-02. 수신 전문 파싱 종료")

        /**
         * 채널 수신부로 디코딩 맵 전달
         */
        outMsgList.add(decodeMap)

        /**
         * 메시지 수신 완료 처리
         */
        msgCompleted = true
    }
}