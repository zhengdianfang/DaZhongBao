package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by dfgzheng on 30/07/2017.
 */
object API {
    private val HOST = "http://47.92.128.105/api/"
    val objectMapper = ObjectMapper().registerKotlinModule()
    val appClient  = Retrofit.Builder()
            .baseUrl(HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()

    fun  parseResponse(response: JsonNode): String {
        if (response.get("errCode").asInt() == 0) {
            return response.get("data").toString()
        }
        throw CException(response.get("msg").asText(), response.get("errCode").asInt())
    }
}
