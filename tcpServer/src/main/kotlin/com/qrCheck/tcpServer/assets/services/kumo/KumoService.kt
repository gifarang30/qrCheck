package com.qrCheck.tcpServer.assets.services.kumo

import com.qrCheck.tcpServer.assets.services.kumo.vo.*
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional


/**
 * 금우 서비스 인터페이스
 */
interface KumoService {

    /**
     * 차량 정보 조회(차량 번호)
     * @param carNum 차량 번호
     * @return CarVO 차랑 정보 VO
     */
    @Throws(Exception::class)
    fun getCarInfo(carNum: String): CarVO?

    /**
     * 차량 정보 조회(차량 고유 시퀀스)
     * @param carSeq 차량 고유 시퀀스
     * @return CarVO 차랑 정보 VO
     */
    @Throws(Exception::class)
    fun getCarInfoAtCarSeq(carSeq: Int): CarVO?

    /**
     * 당일 운행 예약 정보 추출
     * @param carSeq 차량 고유 시퀀스
     * @param rsvSeq 예약 고유 시퀀스
     * @param memberId 회원 아이디
     * @return 예약 정보 VO
     */
    @Throws(Exception::class)
    fun getRsvInfo(carSeq: Int, rsvSeq: Int, memberId: String): RsvVO?

    /**
     * 차량 위치 정보 입력
     * @param carPosVO 차량 위치 정보 VO
     * @return Boolean
     */
    @Throws(Exception::class)
    @Transactional(value = "txMgrKumo", propagation = Propagation.REQUIRED)
    fun insertCarPos(carPosVO: CarPosVO)

    /**
     * QR 체크인 입력
     * @param rsvQrChkInVO QR 체크인 이력 VO
     * @return Boolean
     */
    @Throws(Exception::class)
    @Transactional(value = "txMgrKumo", propagation = Propagation.REQUIRED)
    fun insertQrChkIn(rsvQrChkInVO: RsvQrChkInVO)

    /**
     * 테마여행 예약 정보 추출
     * @param themeResId 테마여행 예약 고유 번호
     * @return 테마여행 예약 정보 VO
     */
    @Throws(Exception::class)
    fun getThemeInfo(themeResId: Int): ThemeVO?
}