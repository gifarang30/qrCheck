package com.qrCheck.tcpServer.assets.services.netty

import com.qrCheck.tcpServer.assets.services.crypt.CryptService
import com.qrCheck.tcpServer.assets.services.jwt.JwtService
import com.qrCheck.tcpServer.assets.services.kumo.KumoService
import com.qrCheck.tcpServer.assets.services.kumo.vo.CarPosVO
import com.qrCheck.tcpServer.assets.services.kumo.vo.RsvQrChkInVO
import com.qrCheck.tcpServer.config.props.PropCmm
import com.qrCheck.tcpServer.config.props.PropRes
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import org.json.simple.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * 금우 수신 핸들러
 */
class NettyHandler(

    private val propCmm: PropCmm,
    private val propRes: PropRes,

    private val cryptService: CryptService,

    private val jwtService: JwtService,

    private val kumoService: KumoService

) : ChannelInboundHandlerAdapter() {

    /**
     * 로거
     */
    private val logger: Logger = LoggerFactory.getLogger(NettyHandler::class.java)

    /**
     * 채널 수신부
     */
    @Throws(NettyHandlerException::class)
    override fun channelRead(ctx: ChannelHandlerContext, inMsg: Any) {

        // 맵으로 디코딩 처리된 전문
        val decodeMap = inMsg as MutableMap<*, *>

        /**
         * 인코딩 맵
         */
        val encodeMap = mutableMapOf<String, Any>()

        /**
         * JWT 기간 만료 여부(JWT 디코드 중 에러 발생시에도 true)
         */
        val expired = decodeMap["expired"] as Boolean
        if (expired) {

            encodeMap["resCd"] = propRes.resCd["cd-jwt-decode-error"].toString()
            encodeMap["resMsg"] = "JWT 디코드 에러 발생"

        } else {

            /**
             * 주제 'kumo' 수신 전문만 처리
             */
            if (decodeMap["sub"] != "kumo") {
                throw NettyHandlerException("주제 영역이 달라 핸들링 종료 처리")
            }

            /**
             * 처리 구분 코드
             */
            val actCd = decodeMap["actCd"] as String

            /**
             * actCd = 101 [ 버스 탑승 체크 ]
             */
            if (actCd == "101") {

                /**
                 * 차량 고유 시퀀스
                 */
                val carSeq = (decodeMap["carSeq"] as Double).toInt()

                /**
                 * 회원 아이디
                 */
                val memberId = decodeMap["memberId"] as String

                /**
                 * actVal = 예약 시퀀스
                 */
                val rsvSeq = Integer.parseInt(decodeMap["actVal"] as String)

                /**
                 * 예약 정보 조회
                 */
                val rsvVO = kumoService.getRsvInfo(carSeq, rsvSeq, memberId)

                /**
                 * 차량번호 맞는지 확인
                 * 조회 된 예약 정보에서 차량 고유 시퀀스 확인 안되면 통과
                 */
                if (rsvVO != null) {

                    /**
                     * 차량 번호 확인 절차
                     */
                    if (rsvVO.carSeqStr.isNotBlank()) {
                        val carSeqArr = rsvVO.carSeqStr.split(",")
                        if (carSeqArr.count() >= rsvVO.carOrder
                            && rsvVO.carOrder > 0) {

                            /**
                             * 예약 정보의 차량 고유 시퀀스
                             */
                            val rsvCarSeq = Integer.parseInt(carSeqArr[rsvVO.carOrder - 1])

                            /**
                             * 차량 번호 다른지 확인
                             */
                            if (rsvCarSeq != carSeq) {
                                val realCarInfo = kumoService.getCarInfoAtCarSeq(rsvCarSeq)
                                encodeMap["resCd"] = propRes.resCd["cd-valid"].toString()
                                if (realCarInfo != null) {
                                    encodeMap["resMsg"] = "${rsvVO.memberNm}님 예약한 차량이 아닙니다. ${realCarInfo.carNum}에 탑승하세요."
                                } else {
                                    encodeMap["resMsg"] = "${rsvVO.memberNm}님 예약한 차량이 아닙니다."
                                }
                            }
                        }
                    }

                    /**
                     * 아직 결과 정보가 없는 경우 예약 정보 확인 처리
                     */
                    if (encodeMap.isEmpty()) {
                        kumoService.insertQrChkIn(RsvQrChkInVO(
                            rsvSeq, actCd, 0, "", ""
                        ))
                        encodeMap["resCd"] = propRes.resCd["cd-ok"].toString()
                        encodeMap["resMsg"] = "${rsvVO.memberNm}님 예약 확인 되었습니다."
                    }

                } else {
                    encodeMap["resCd"] = propRes.resCd["cd-valid"].toString()
                    encodeMap["resMsg"] = "예약을 확인 할수 없습니다."
                }

            /**
             * actCd = 109 [ 차량 실제 번호 확인 ]
             */
            } else if (actCd == "109") {

                /**
                 * 차량 번호
                 */
                val carNum = decodeMap["carNum"] as String

                /**
                 * 차량 정보 추출
                 */
                val carVO = kumoService.getCarInfo(carNum)
                if (carVO != null) {
                    encodeMap["resCd"] = propRes.resCd["cd-ok"].toString()
                    encodeMap["resMsg"] = "차량 번호 확인 되었습니다."
                    encodeMap["resVal"] = carVO
                } else {
                    encodeMap["resCd"] = propRes.resCd["cd-valid"].toString()
                    encodeMap["resMsg"] = "시스템에 등록 되지 않는 차량 번호 입니다."
                }

            /**
             * actCd = 201 [ 차량 위치 정보 입력 ]
             */
            } else if (actCd == "201") {

                /**
                 * 시도만 할뿐 에러 처리는 안함
                 */
                val carSeq = (decodeMap["carSeq"] as Double).toInt()
                val carLat = decodeMap["carLat"] as String
                val carLng = decodeMap["carLng"] as String
                try {
                    kumoService.insertCarPos(CarPosVO(carSeq, carLat, carLng))
                    encodeMap["resCd"] = propRes.resCd["cd-ok"].toString()
                    encodeMap["resMsg"] = "차랑번호 : $carSeq, 좌표 [$carLat, $carLng] 입력 완료"
                } catch (e: Exception) {
                    encodeMap["resCd"] = propRes.resCd["cd-error"].toString()
                    encodeMap["resMsg"] = "차량 위치 정보 입력중 에러 발생"
                }

            /**
             * actCd = 301 [ 테마여행 탑승 체크 ]
             */
            } else if (actCd == "301") {

                /**
                 * actVal = 테마여행 예약 고유 번호
                 */
                val themeResId = Integer.parseInt(decodeMap["actVal"] as String)

                /**
                 * 테마여행 예약 정보 조회
                 */
                val themeVO = kumoService.getThemeInfo(themeResId)

                /**
                 * 조회 된 테마여행 예약 정보에서 차량 고유 시퀀스 확인 안되면 통과
                 */
                if (themeVO != null) {

                    /**
                     * 아직 결과 정보가 없는 경우 예약 정보 확인 처리
                     */
                    if (encodeMap.isEmpty()) {
                        kumoService.insertQrChkIn(RsvQrChkInVO(
                            themeResId, actCd, 0, "", ""
                        ))
                        encodeMap["resCd"] = propRes.resCd["cd-ok"].toString()
                        encodeMap["resMsg"] = "${themeVO.themeName} 예약 확인 되었습니다."
                    }

                } else {
                    encodeMap["resCd"] = propRes.resCd["cd-valid"].toString()
                    encodeMap["resMsg"] = "예약을 확인 할수 없습니다."
                }
            }
        }

        val jwtStr = jwtService.encodeJwt("kumo", encodeMap)
        ctx.writeAndFlush(Unpooled.wrappedBuffer(jwtStr.toByteArray()))
        ctx.close()
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.flush()
    }

    /**
     * Exception 발생 처리
     */
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {

        /**
         * 에러 정보 인코딩 맵 생성
         */
        val encodeMap = mutableMapOf<String, Any>()
        encodeMap["resCd"] = propRes.resCd["cd-error"].toString()
        encodeMap["resMsg"] = cause.localizedMessage

        /**
         * 에러 발생시 JWT 인코딩 후 바로 응답 처리
         */
        logger.debug(JSONObject(encodeMap).toString())
        ctx.writeAndFlush(Unpooled.wrappedBuffer(jwtService.encodeJwt("kumo", encodeMap).toByteArray()))
        //ctx.writeAndFlush(Unpooled.wrappedBuffer(JSONObject(encodeMap).toString().toByteArray()))
        ctx.close()
    }
}