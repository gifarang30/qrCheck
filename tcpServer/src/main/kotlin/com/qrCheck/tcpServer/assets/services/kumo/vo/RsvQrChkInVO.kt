package com.qrCheck.tcpServer.assets.services.kumo.vo

/**
 * QR 체크인 이력 VO
 */
data class RsvQrChkInVO(
    val rsvSeq: Int,
    val rsvCd: String,
    val regTimestamp: Long,
    val regDate: String,
    val regHour: String
) : java.io.Serializable