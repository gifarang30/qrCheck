package com.qrCheck.tcpServer.assets.services.kumo

import com.qrCheck.tcpServer.assets.services.kumo.vo.*
import org.springframework.stereotype.Service
import javax.annotation.Resource

/**
 * 금우 서비스 구현부
 */
@Service("kumoServiceImpl")
class KumoServiceImpl(

    @Resource(name = "kumoMapper")
    val kumoMapper: KumoMapper

) : KumoService {

    /**
     * 차량 정보 조회(차량 번호)
     * @param carNum 차량 번호
     * @return CarVO 차랑 정보 VO
     */
    override fun getCarInfo(carNum: String): CarVO? {
        val carParamMap = mutableMapOf<String, Any>()
        carParamMap["carNum"] = carNum
        return kumoMapper.getCarInfo(carParamMap)
    }

    /**
     * 차량 정보 조회(차량 고유 시퀀스)
     * @param carSeq 차량 고유 시퀀스
     * @return CarVO 차랑 정보 VO
     */
    override fun getCarInfoAtCarSeq(carSeq: Int): CarVO? {
        val carParamMap = mutableMapOf<String, Any>()
        carParamMap["carSeq"] = carSeq
        return kumoMapper.getCarInfo(carParamMap)
    }

    /**
     * 당일 운행 예약 정보 체크
     * @param carSeq 차량 고유 시퀀스
     * @param rsvSeq 예약 고유 시퀀스
     * @param memberId 회원 아이디
     * @return Boolean
     */
    override fun getRsvInfo(carSeq: Int, rsvSeq: Int, memberId: String): RsvVO? {
        val rsvParamMap = mutableMapOf<String, Any>()
        rsvParamMap["rsvSeq"] = rsvSeq
        rsvParamMap["memberId"] = memberId
        return kumoMapper.getRsvInfo(rsvParamMap)
    }

    /**
     * 차량 위치 정보 입력
     * @param carPosVO 차량 위치 정보 VO
     * @return Boolean
     */
    override fun insertCarPos(carPosVO: CarPosVO) {
        kumoMapper.insertCarPos(carPosVO)
    }

    /**
     * QR 체크인 입력
     * @param rsvQrChkInVO QR 체크인 이력 VO
     * @return Boolean
     */
    override fun insertQrChkIn(rsvQrChkInVO: RsvQrChkInVO) {
        try {
            kumoMapper.insertQrChkIn(rsvQrChkInVO)
        } catch (_: Exception) {}
    }

    /**
     * 테마여행 예약 정보 추출
     * @param themeResId 테마여행 예약 고유 번호
     * @return 테마여행 예약 정보 VO
     */
    override fun getThemeInfo(themeResId: Int): ThemeVO? {
        val rsvParamMap = mutableMapOf<String, Any>()
        rsvParamMap["themeResId"] = themeResId
        return kumoMapper.getThemeInfo(rsvParamMap)
    }
}