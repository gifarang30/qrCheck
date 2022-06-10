package com.qrCheck.tcpServer.assets.services.jwt

import com.qrCheck.tcpServer.config.props.PropCmm
import com.qrCheck.tcpServer.config.props.PropCrypt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.netty.buffer.ByteBuf
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.nio.charset.Charset
import java.util.*

/**
 * JWT 서비스 구현부
 */
@Service("jwtServiceImpl")
class JwtServiceImpl(
    private val propCmm: PropCmm,
    private val propCrypt: PropCrypt
): JwtService {

    /**
     * JWT 비밀키
     */
    private var jwtSecretKey = ""
    init {
        jwtSecretKey = Base64.getEncoder().encodeToString(propCrypt.jwtSecretKey.toByteArray())
    }

    /**
     * 로거
     */
    private val logger: Logger = LoggerFactory.getLogger(JwtServiceImpl::class.java)

    /**
     * JWT 인코딩
     * @param subject JWT Subject 핸들러 분기 코드로 활용
     * @param jwtDataMap JWT 데이터 맵
     * @param expiredTime 인증 만료 시간
     * @return String JWT 문자열
     */
    override fun encodeJwt(subject: String, jwtDataMap: MutableMap<String, Any>, expiredTime: Long): String {

        val claims = Jwts.claims().setSubject(subject)
        for (jwtData in jwtDataMap) {
            claims[jwtData.key] = jwtData.value
        }

        // 현재 시간
        val now = Date()

        return Jwts.builder()

            // 토큰 정보 저장
            .setClaims(claims)

            // 토큰 발행 시간 정보
            .setIssuedAt(now)

            // 인증 만료 시간 셋팅
            .setExpiration(Date(now.time + expiredTime))

            // 암호화 알고리즘과 비밀키 셋팅
            .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
            .compact()
    }

    /**
     * JWT 디코딩
     * @param inMsgBuf 수신 전문 ByteBuf
     * @return 파싱된 맵 정보 'expired' 값 true 이면 만기 or 오류 JWT
     */
    @Throws(Exception::class)
    override fun decodeJwt(inMsgBuf: ByteBuf): MutableMap<String, Any> {
        val resultMap = mutableMapOf<String, Any>()
        try {
            val jwtStr = inMsgBuf.toString(Charset.forName(propCmm.msgCharset))
            val claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(jwtStr)
            for (claimsIter in claims.body) {
                resultMap[claimsIter.key] = claimsIter.value
            }
            resultMap["expired"] = claims.body.expiration.before(Date())
        } catch (e: Exception) {
            resultMap["expired"] = true
        }
        return resultMap
    }
}