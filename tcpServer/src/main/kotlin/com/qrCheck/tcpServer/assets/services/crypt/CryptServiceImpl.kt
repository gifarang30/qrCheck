package com.qrCheck.tcpServer.assets.services.crypt

import com.qrCheck.tcpServer.config.props.PropCrypt
import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.binary.Hex
import org.springframework.stereotype.Service
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

/**
 * 암복호화 서비스 구현부
 */
@Service("cryptServiceImpl")
class CryptServiceImpl(
    private val propCrypt: PropCrypt
): CryptService {
    
    /**
     * 복호화 함수
     * @param str 복호화 대상 문자열
     * @return 복호화 된 문자열
     */
    override fun decrypt(str: String): String {
        val spec: KeySpec =
            PBEKeySpec(propCrypt.cryptPassword.toCharArray(), getHex(propCrypt.cryptSalt), propCrypt.cryptIterationCount, propCrypt.cryptKeyLength)
        val key: SecretKey = SecretKeySpec(getSecretKeyIns().generateSecret(spec).encoded, propCrypt.cryptAlgorithm)
        val cipherIns = getCipherIns()
        cipherIns.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(getHex(propCrypt.cryptIv)))
        val decrypted = cipherIns.doFinal(Base64.decodeBase64(str))
        return String(decrypted, charset("UTF-8"))
    }

    /**
     * 암호화 함수
     * @param str 암호화 대상 문자열
     * @return 암호화 된 문자열
     */
    override fun encrypt(str: String): String {
        val spec: KeySpec =
            PBEKeySpec(propCrypt.cryptPassword.toCharArray(), getHex(propCrypt.cryptSalt), propCrypt.cryptIterationCount, propCrypt.cryptKeyLength)
        val key: SecretKey = SecretKeySpec(getSecretKeyIns().generateSecret(spec).encoded, propCrypt.cryptAlgorithm)
        val cipherIns = getCipherIns()
        cipherIns.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(getHex(propCrypt.cryptIv)))
        val encrypted = cipherIns.doFinal(str.toByteArray(charset("UTF-8")))
        return String(Base64.encodeBase64(encrypted))
    }

    /**
     * 비밀키 인스턴스 추출
     */
    private fun getSecretKeyIns(): SecretKeyFactory {
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
    }

    /**
     * Cipher 인스턴스 추출
     */
    private fun getCipherIns(): Cipher {
        return Cipher.getInstance("AES/CBC/PKCS5Padding")
    }

    /**
     * HEX 값 추출
     */
    private fun getHex(str: String): ByteArray {
        return Hex.decodeHex(str.toCharArray())
    }
}