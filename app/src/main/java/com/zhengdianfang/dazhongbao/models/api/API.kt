package com.zhengdianfang.dazhongbao.models.api

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by dfgzheng on 30/07/2017.
 */
object API {
    private val HOST = "http://47.92.128.105/api"
    val appClient  = Retrofit.Builder()
            .baseUrl(HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
}
