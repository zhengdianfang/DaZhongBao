package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by dfgzheng on 20/08/2017.
 */
interface AdvertApi {

    @FormUrlEncoded
    @POST("index/banner")
    fun fetchAdvertBanner(@Field("token")token: String): Observable<JsonNode>
}