package com.zhengdianfang.dazhongbao.helpers

import com.orhanobut.logger.Logger
import java.util.*

/**
 * Created by dfgzheng on 21/08/2017.
 */
object DateUtils {

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
}