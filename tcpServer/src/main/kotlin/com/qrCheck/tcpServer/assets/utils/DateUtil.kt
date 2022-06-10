package com.qrCheck.tcpServer.assets.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 오늘 날짜 값 구하기
     * @param format 날짜 포맷
     * @return 문자열
     */
    fun getToday(format: String = "yyyy-MM-dd"): String {
        val now = Date()
        val today = SimpleDateFormat(format, Locale.KOREA)
        return today.format(now)
    }

    /**
     * 년도
     * @param format 날짜 포맷
     * @return 문자열
     */
    val year: Int
        get() = getToday("yyyy").toInt()

    /**
     * 월
     * @param format 날짜 포맷
     * @return 문자열
     */
    val month: Int
        get() = getToday("MM").toInt()

    /**
     * 일
     * @param format 날짜 포맷
     * @return 문자열
     */
    val day: Int
        get() = getToday("dd").toInt()

    /**
     * 오늘 요일 값 구하기
     * @return 문자열
     */
    val week: String
        get() {
            val now = Date()
            val week = SimpleDateFormat("E", Locale.KOREA)
            return week.format(now)
        }

    /**
     * 현재 타입스템프값
     * @return long
     */
    @get:Throws(ParseException::class)
    val currentTimestamp: Long
        get() = (System.currentTimeMillis() / 1000).toInt().toLong()

    /**
     * 날짜형식의 문자열을 타임스탬프 값으로 변경
     * @return long
     */
    @Throws(ParseException::class)
    fun dateToTimestamp(dateStr: String?): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        return (sdf.parse(dateStr).time / 1000)
    }

    /**
     * 파라미터에서 키값에 해당하는 날짜형식의 문자열을 타임스탬프 값으로 변경
     * @param paramMap
     * @param key
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun dateToTimestamp(paramMap: MutableMap<String?, Any?>, key: String) {
        val dateStr = paramMap[key] as String?
        if (!dateStr.isNullOrBlank()) {
            paramMap[key + "Time"] = dateToTimestamp(dateStr)
        }
    }

    /**
     * 파라미터에서 키값에 해당하는 날짜형식의 문자열을 타임스탬프 값으로 변경
     * @param paramMap
     * @param keyArr
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun dateToTimestamp(paramMap: MutableMap<String?, Any?>, keyArr: Array<String>) {
        for (key in keyArr) {
            // 날짜 검색 범위 마지막날짜는 + 1 일
            if (key == "send") {
                paramMap[key] = addDay(paramMap[key] as String?, 1)
            }
            dateToTimestamp(paramMap, key)
        }
    }

    /**
     * 오늘 날짜 시작 밀리타임값
     * @return long
     */
    @get:Throws(ParseException::class)
    val todayStartTime: Long
        get() {
            val now = Date()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
            val trasnNow = sdf.format(now)
            return (sdf.parse(trasnNow).time / 1000)
        }

    /**
     * 날짜 더하기
     * @param dateStr - yyyy-MM-dd
     * @param day - 추가할 날짜
     * @return
     */
    fun addDay(dateStr: String?, day: Int): String? {
        if (dateStr.isNullOrBlank()) {
            return null
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        val cal = Calendar.getInstance()
        try {
            val date = sdf.parse(dateStr)
            cal.time = date
            cal.add(Calendar.DATE, day)
        } catch (e: ParseException) {
        }
        return sdf.format(cal.time)
    }

    /**
     * 오늘 인지 체크
     * @param date - 날짜 타임스탬프
     * @return Boolean
     */
    fun isToday(date: Long): Boolean {
        val now = Calendar.getInstance()
        val timeToCheck = Calendar.getInstance()
        timeToCheck.timeInMillis = date * 1000
        val nowYear = now[Calendar.YEAR]
        val nowMon = now[Calendar.MONTH]
        val nowDay = now[Calendar.DAY_OF_YEAR]
        val checkYear = timeToCheck[Calendar.YEAR]
        val checkMon = timeToCheck[Calendar.MONTH]
        val checkDay = timeToCheck[Calendar.DAY_OF_YEAR]
        return nowYear == checkYear && nowMon == checkMon && nowDay == checkDay
    }

    /**
     * 타임스탬프를 날짜 문자열로 변환
     * @param date - 날짜 타임스탬프
     * @param format - 날짜 포맷
     * @return Boolean
     * @throws ParseException
     */
    fun getDateStr(date: Long, format: String = "yyyy.MM.dd HH:mm"): String {
        val currentDate = Date(date * 1000)
        val df = SimpleDateFormat(format, Locale.KOREA)
        return df.format(currentDate)
    }

    /**
     * 해당 년월의 말일 구하기
     * @param year - 년
     * @param mon - 월
     * @return int
     */
    fun getLastDay(year: Int, mon: Int): Int {
        val calendar = Calendar.getInstance()
        calendar[year, mon - 1] = 1
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * D-DAY 구하기
     * @param dateStr - 날짜 문자열(yyyy-MM-dd)
     * @return Integer
     */
    fun getDday(dateStr: String): Int {

        // 숫자만 남기고 삭제
        var dateStr = dateStr
        dateStr = dateStr.replace("[^0-9]".toRegex(), "")

        // 년 월 일 추출
        val year = dateStr.substring(0, 4).toInt()
        val mon = dateStr.substring(4, 6).toInt()
        val day = dateStr.substring(6).toInt()

        // 날짜 정보 생성
        val todayCal = Calendar.getInstance()
        val ddayCal = Calendar.getInstance()
        ddayCal[year, mon] = day

        // D-DAY 계산
        val dday = ddayCal.timeInMillis / 86400000 - todayCal.timeInMillis / 86400000 + 1
        return dday.toInt()
    }
}