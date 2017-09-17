package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by dfgzheng on 26/07/2017.
 */
interface MessageApi {

    @FormUrlEncoded
    @POST("index/getMessageCount")
    fun fetchNotifyMessageCount(@Field("token") token: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("index/messages")
    fun fetchMessageListByType(@Field("token") token: String, @Field("icon_type")icon_type: Int, @Field("pageNumber")pageNumber: Int = 0): Observable<JsonNode>
}