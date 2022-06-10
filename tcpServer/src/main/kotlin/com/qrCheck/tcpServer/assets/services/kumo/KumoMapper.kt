package com.qrCheck.tcpServer.assets.services.kumo

import com.qrCheck.tcpServer.assets.services.kumo.vo.*
import org.apache.ibatis.annotations.Mapper
import org.springframework.dao.DataAccessException

/**
 * 금우 Mapper
 */
@Mapper
interface KumoMapper {

    /**
     * 차량 정보 조회
     * @param paramMap 파라미터 맵
     * @return CarVO 차량 정보 VO
     */
    @Throws(DataAccessException::class)
    fun getCarInfo(paramMap: MutableMap<String, Any>): CarVO?

    /**
     * 오늘자 예약 정보 조회
     * @param paramMap 파라미터 맵
     * @return RsvVO 예약 정보 VO
     */
    @Throws(DataAccessException::class)
    fun getRsvInfo(paramMap: MutableMap<String, Any>): RsvVO?

    /**
     * 차량 위치 정보 입력
     * @param carPosVO 차량 위치 정보 VO
     * @exception DataAccessException
     */
    @Throws(DataAccessException::class)
    fun insertCarPos(carPosVO: CarPosVO)

    /**
     * QR 체크인 입력
     * @param rsvQrChkInVO QR 체크인 이력 VO
     * @exception DataAccessException
     */
    @Throws(DataAccessException::class)
    fun insertQrChkIn(rsvQrChkInVO: RsvQrChkInVO)

    /**
     * 테마여행 예약 정보 조회
     * @param paramMap 파라미터 맵
     * @return ThemeVO 테마여행 예약 정보 VO
     */
    @Throws(DataAccessException::class)
    fun getThemeInfo(paramMap: MutableMap<String, Any>): ThemeVO?
}