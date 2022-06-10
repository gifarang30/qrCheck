package com.qrCheck.tcpServer.assets.services.kumo.vo

/**
 * 예약 정보 VO
 */
data class RsvVO(
    val memberId: String,
    val memberNm: String,
    val carSeqStr: String,
    val carOrder: Int,
    val seatNum: Int,
    //val runDate: String
) : java.io.Serializable