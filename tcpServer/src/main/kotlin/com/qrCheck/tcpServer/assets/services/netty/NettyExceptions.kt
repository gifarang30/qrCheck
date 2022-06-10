package com.qrCheck.tcpServer.assets.services.netty

import java.lang.Exception

/**
 * Netty 디코더 예외
 */
class NettyDecoderException(msg: String) : Exception(msg)

/**
 * Netty 핸들러 예외
 */
class NettyHandlerException(msg: String) : Exception(msg)