package com.qrCheck.tcpServer.assets.services.kumo.vo

/**
 * 차량 좌표 정보 VO
 */
data class CarPosVO(
    val carSeq: Int,
    val carLat: String,
    val carLng: String
) : java.io.Serializable