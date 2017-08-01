package com.zhengdianfang.dazhongbao.helpers

import com.fasterxml.jackson.databind.JavaType
import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Created by dfgzheng on 30/07/2017.
 */
object JsonUtils {

    fun object2Json(obj: Any): String? {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(obj)
    }

    fun <T> json2Object(json: String?, t: JavaType): T {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue<T>(json, t)
    }
}