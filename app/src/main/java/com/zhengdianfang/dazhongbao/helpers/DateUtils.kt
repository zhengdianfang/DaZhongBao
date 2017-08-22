package com.zhengdianfang.dazhongbao.helpers

import com.orhanobut.logger.Logger
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

    fun diffTime(startDate: Date, endDate: Date): Day {
        val nd = 1000*24*60*60//一天的毫秒数
        val nh = 1000*60*60//一小时的毫秒数
        val nm = 1000*60//一分钟的毫秒数
        val ns = 1000//一秒钟的毫秒数long diff;try {
        val diff = endDate.time - startDate.time
        Logger.d("diff time $diff  start: ${startDate.time} end: ${endDate.time}")
        val day = diff/nd//计算差多少天
        val hour = diff%nd/nh//计算差多少小时
        val min = diff%nd%nh/nm//计算差多少分钟
        val sec = diff%nd%nh%nm/ns//计算差多少秒//输出结果s
        return Day(day, hour, min, sec)
    }

    fun formatDateStr2Desc(nowTime: Long, curTime: Long): String {
        var curTime = curTime
        if (curTime < 10000000000L) {
            curTime = curTime * 1000
        }
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

}