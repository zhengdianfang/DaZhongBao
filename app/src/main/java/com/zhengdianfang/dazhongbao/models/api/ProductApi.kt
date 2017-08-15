package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by dfgzheng on 10/08/2017.
 */
interface ProductApi {

    @FormUrlEncoded
    @POST("products/plist")
    fun getProductList(@Field("token") token: String?, @Field("pageNumber") pageNumber: Int,
                       @Field("check_status") checkStatus: Int): Observable<JsonNode>


    @FormUrlEncoded
    @POST("products/push")
    fun pushProduct(@Field("token") token: String, @Field("sharesCode") sharesCodes: String,
                    @Field("companyName") companyName: String, @Field("basicUnitPrice") basicUnitPrice: Double,
                    @Field("soldCount") soldCount: Int,@Field("limitTime") limitTime: Long,
                    @Field("notes") notes: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/getProductInfo")
    fun getProductInfo(@Field("token") token: String, @Field("productId") productId: Long): Observable<JsonNode>
}