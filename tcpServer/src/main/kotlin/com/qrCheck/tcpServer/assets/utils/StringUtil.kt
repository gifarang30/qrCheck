package com.qrCheck.tcpServer.assets.utils

import io.netty.buffer.ByteBuf
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset

object StringUtil {

    /**
     * 로거
     */
    private val logger: Logger = LoggerFactory.getLogger(StringUtil::class.java)

    /**
     * 메시지를 JSON 오브젝트로 파싱
     * @param msg
     */
    @Throws(Exception::class)
    fun parseJson(msg: ByteBuf): JSONObject {

        var jsonObj = JSONObject()

        // 메시지 비었는지 체크
        if (msg.readableBytes() == 0) {
            return jsonObj
        }

        // 메시지 파싱 시작
        val parser = JSONParser()
        try {
            jsonObj = parser.parse(msg.toString(Charset.defaultCharset()).trim { it <= ' ' }) as JSONObject
        } catch (e: Exception) {
            logger.error("전문 parse error : " + e.message)
        }
        return jsonObj
    }
}