package com.zhengdianfang.dazhongbao.helpers

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by dfgzheng on 21/08/2017.
 */
object DateUtils {

    private val ONE_SECOND_AGO = "秒前"
    private val ONE_MINUTE_AGO = "分钟前"
    private val ONE_HOUR_AGO = "小时前"
    private val ONE_DAY_AGO = "天前"
    private val ONE_MONTH_AGO = "月前"
    private val ONE_YEAR_AGO = "年前"

    private val ONE_MINUTE = 60000L
    private val ONE_HOUR = 3600000L
    private val ONE_DAY = 86400000L
    private val ONE_WEEK = 604800000L

    data class Day(var day: Long, var hour: Long, var min: Long, var sec: Long)

    fun diffTime(startTime: Long, endTime: Long): Day {
        val startTime = changeTimeLenght(startTime)
        val endTime = changeTimeLenght(endTime)
        val nd = 1000*24*60*60//一天的毫秒数
        val nh = 1000*60*60//一小时的毫秒数
        val nm = 1000*60//一分钟的毫秒数
        val ns = 1000//一秒钟的毫秒数long diff;try {
        val diff = endTime - startTime
        val day = diff/nd//计算差多少天
        val hour = diff%nd/nh//计算差多少小时
        val min = diff%nd%nh/nm//计算差多少分钟
        val sec = diff%nd%nh%nm/ns//计算差多少秒//输出结果s
        return Day(day, hour, min, sec)
    }

    fun formatDateStr2Desc(nowTime: Long, curTime: Long): String {
        var curTime = changeTimeLenght(curTime)
        val delta = Math.abs(nowTime - curTime)
        if (delta < 1L * ONE_MINUTE) {
            val seconds = toSeconds(delta)
            return "刚刚"
        }
        if (delta < 45L * ONE_MINUTE) {
            val minutes = toMinutes(delta)
            return (if (minutes <= 0) 1 else minutes).toString() + ONE_MINUTE_AGO
        }
        if (delta < 24L * ONE_HOUR) {
            val hours = toHours(delta)
            return (if (hours <= 0) 1 else hours).toString() + ONE_HOUR_AGO
        }
        if (delta < 30L * ONE_DAY) {
            val days = toDays(delta)
            return (if (days <= 0) 1 else days).toString() + ONE_DAY_AGO
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            val months = toMonths(delta)
            return (if (months <= 0) 1 else months).toString() + ONE_MONTH_AGO
        } else {
            val years = toYears(delta)
            return (if (years <= 0) 1 else years).toString() + ONE_YEAR_AGO
        }
    }

    private fun toSeconds(date: Long): Long {
        return date / 1000L
    }

    private fun toMinutes(date: Long): Long {
        return toSeconds(date) / 60L
    }

    private fun toHours(date: Long): Long {
        return toMinutes(date) / 60L
    }

    private fun toDays(date: Long): Long {
        return toHours(date) / 24L
    }

    private fun toMonths(date: Long): Long {
        return toDays(date) / 30L
    }

    private fun toYears(date: Long): Long {
        return toMonths(date) / 365L
    }

    fun changeTimeLenght(time: Long): Long {
        var time = time
        if (time < 10000000000L) {
            time *= 1000
        }
        return time
    }

    fun formatTime(time: Long, format: String = "yyyy-MM-dd hh:mm:ss" ): String {
        val format = SimpleDateFormat(format)
        return format.format(Date(DateUtils.changeTimeLenght(time)))
    }

    fun calTimeDistanceByHH_MM_SS(startTime: Long, endTime: Long): String {
        val startTime = changeTimeLenght(startTime)
        val endTime = changeTimeLenght(endTime)
        val delta = Math.abs(endTime - startTime)
        val hour = toHours(delta)
        val min = toMinutes(delta - hour * ONE_HOUR)
        val sec = toSeconds(delta - hour * ONE_HOUR - min * ONE_MINUTE)
        val hourString = if (hour < 10) "0$hour" else hour.toString()
        val minString = if (min < 10) "0$min" else min.toString()
        val secString = if (sec < 10) "0$sec" else sec.toString()
        return "$hourString:$minString:$secString"
    }
}