package com.qrCheck.tcpServer.assets.services.kumo.vo

/**
 * 차량 정보 VO
 */
data class CarVO(
    val carSeq: Int,
    val carNum: String,
    val crewName: String,
    val crewTel: String
) : java.io.Serializable