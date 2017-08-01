package com.zhengdianfang.dazhongbao.helpers

import android.content.Context


/**
 * Created by dfgzheng on 30/07/2017.
 */
class FileUtils{
        fun readRawFile(context: Context?, rawId: Int): String? {
            val inputStream = context?.resources?.openRawResource(rawId)
            val inputString = inputStream?.bufferedReader().use { it?.readText() }
            return inputString
        }

        fun readRawFileForMock() {
            val inputStream = this.javaClass.classLoader.getResourceAsStream("raw/news_list")
        }
}