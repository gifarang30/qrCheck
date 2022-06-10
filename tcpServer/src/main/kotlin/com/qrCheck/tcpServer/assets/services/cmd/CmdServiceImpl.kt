package com.qrCheck.tcpServer.assets.services.cmd

import com.qrCheck.tcpServer.TcpServerApplication
import com.qrCheck.tcpServer.config.props.PropCmd
import com.qrCheck.tcpServer.config.props.PropRes
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

/**
 * OS 명령어 실행 구현부
 */
@Service("cmdServiceImpl")
class CmdServiceImpl(
    private val propRes: PropRes,
    private val propCmd: PropCmd
): CmdService {

    /**
     * 명령어 실행
     * @param cmd - 명령어
     * @param cmdEncoding - 결과 인코딩
     * @return
     */
    override fun excuteCmd(cmd: String, cmdEncoding: String): String {

        // 처리 결과 메시지
        var resMsg = ""

        // 명령어가 없으면 처리 제외
        if (cmd.isBlank()) {
            return resMsg
        }

        // 명령어 필터링 체크
        val chkCmdResult = chkCmdFilter(cmd)
        if (chkCmdResult != propRes.resMsg["msg-ok"]) {
            return chkCmdResult
        }
        var process: Process? = null
        val runtime = Runtime.getRuntime()
        val successOutput = StringBuffer() // 성공 스트링 버퍼
        val errorOutput = StringBuffer() // 오류 스트링 버퍼
        var successBufferReader: BufferedReader? = null // 성공 버퍼
        var errorBufferReader: BufferedReader? = null // 오류 버퍼
        var msg = "" // 메시지
        val cmdList: MutableList<String> = ArrayList()

        // 운영체제 구분 (window, window 가 아니면 무조건 linux 로 판단)
        if (System.getProperty("os.name").indexOf("Windows") > -1) {
            cmdList.add("cmd")
            cmdList.add("/c")
        } else {
            cmdList.add("/bin/sh")
            cmdList.add("-c")
        }

        // 명령어 셋팅
        cmdList.add(chkFixedCmd(cmd) + cmd)
        val array = cmdList.toTypedArray()
        try {

            // 명령어 실행
            process = runtime.exec(array)

            // shell 실행이 정상 동작했을 경우
            successBufferReader = BufferedReader(InputStreamReader(process.inputStream, cmdEncoding))
            while (successBufferReader.readLine().also { msg = it } != null) {
                successOutput.append(msg + System.getProperty("line.separator"))
            }

            // shell 실행시 에러가 발생했을 경우
            errorBufferReader = BufferedReader(InputStreamReader(process.errorStream, cmdEncoding))
            while (errorBufferReader.readLine().also { msg = it } != null) {
                errorOutput.append(msg + System.getProperty("line.separator"))
            }

            // 프로세스의 수행이 끝날때까지 대기
            process.waitFor()

            // shell 실행이 정상 종료되었을 경우
            resMsg = if (process.exitValue() == 0) {
                successOutput.toString()
                // shell 실행이 비정상 종료되었을 경우
            } else {
                errorOutput.toString()
            }

            // shell 실행시 에러가 발생
            if (!errorOutput.toString().endsWith("")) {
                resMsg = errorOutput.toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        } finally {
            try {
                process?.destroy()
                successBufferReader?.close()
                errorBufferReader?.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }
        }
        return resMsg
    }

    /**
     * 명령어 필터링 체크
     * @param cmdStr 명령어 문자열
     * @return 필터링 끝난 명령어 문자열
     */
    @Throws(Exception::class)
    override fun chkCmdFilter(cmdStr: String): String {
        val cmd = cmdStr.lowercase(Locale.getDefault())
        var resMsg: String = propRes.resMsg["msg-ok"]!!
        if (cmd.contains(">")) {
            resMsg = "'>'특수문자는 사용할수 없습니다."
            return resMsg
        }

        // 파이프라인 체크
        var cmdPipeArr = arrayOf(cmd)
        if (cmd.contains("|")) {
            cmdPipeArr = cmd.split("\\|".toRegex()).toTypedArray()
        }
        for (cmdPipeStr in cmdPipeArr) {
            var cmdPipe = cmdPipeStr.trim { it <= ' ' }

            // 중간에 공백 있으면 첫 공백 뒤로 제거
            if (cmdPipe.contains(" ")) {
                val cmdArr = cmdPipe.split(" ")
                cmdPipe = cmdArr[0]
            }

            // / 포함시 앞쪽 제거
            if (cmdPipe.contains("/")) {
                val cmdSe1Arr = cmdPipe.split("/")
                cmdPipe = cmdSe1Arr[cmdSe1Arr.size - 1]
            }

            // \ 포함시 앞쪽 제거
            if (cmdPipe.contains("\\")) {
                val cmdSe2Arr = cmdPipe.split("\\")
                cmdPipe = cmdSe2Arr[cmdSe2Arr.size - 1]
            }
            for (filterStr in propCmd.cmdFilterList) {

                // 필터링 문자열로 시작하는지 체크
                if (cmdPipe == filterStr || cmdPipe.startsWith("$filterStr ")) {
                    resMsg = "'$filterStr' 명령어는 사용할수 없습니다."
                    break
                }
            }
            if (resMsg != propRes.resMsg["msg-ok"]) {
                break
            }
        }
        return resMsg
    }

    /**
     * 지정된 명령어 인지 체크
     * @param cmd - 명령어
     * @return 명령어 문자열
     */
    @Throws(Exception::class)
    override fun chkFixedCmd(cmd: String): String {
        var path = ""
        for (cmdStr in propCmd.cmdFixedList) {

            // 지정 명령어로 시작하는지 체크
            if (cmd == cmdStr || cmd.startsWith("$cmdStr ")) {
                path = File(
                    TcpServerApplication::class.java.protectionDomain.codeSource.location.toURI()
                ).path
                if (System.getProperty("os.name").indexOf("Windows") > -1) {
                    path = path.replace("\\bin", "").replace("\\qrCheckTcpServer.jar", "")
                    path = "$path\\cmd\\"
                } else {
                    path = path.replace("/bin", "").replace("/qrCheckTcpServer.jar", "")
                    path = "$path/cmd/"
                }
                break
            }
        }
        return path
    }
    
}