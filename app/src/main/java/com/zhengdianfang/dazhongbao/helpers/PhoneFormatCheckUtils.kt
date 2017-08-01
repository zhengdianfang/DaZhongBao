package com.zhengdianfang.dazhongbao.helpers

import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * Created by dfgzheng on 30/07/2017.
 */
object  PhoneFormatCheckHelper {

    /**
     * 大陆号码或香港号码均可
     */
    @Throws(PatternSyntaxException::class)
    fun isPhoneLegal(str: String): Boolean {
        return isChinaPhoneLegal(str) || isHKPhoneLegal(str)
    }

    /**
     * 大陆手机号码11位数，匹配格式：前三位固定格式+后8位任意数
     * 此方法中前三位格式有：
     * 13+任意数
     * 15+除4的任意数
     * 18+除1和4的任意数
     * 17+除9的任意数
     * 147
     */
    @Throws(PatternSyntaxException::class)
    fun isChinaPhoneLegal(str: String): Boolean {
        val regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(str)
        return m.matches()
    }

    /**
     * 香港手机号码8位数，5|6|8|9开头+7位任意数
     */
    @Throws(PatternSyntaxException::class)
    fun isHKPhoneLegal(str: String): Boolean {
        val regExp = "^(5|6|8|9)\\d{7}$"
        val p = Pattern.compile(regExp)
        val m = p.matcher(str)
        return m.matches()
    }
}