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
                       @Field("check_status") checkStatus: String , @Field("order") order: String): Observable<JsonNode>


    @FormUrlEncoded
    @POST("products/push")
    fun pushProduct(@Field("token") token: String, @Field("sharesCode") sharesCodes: String,
                    @Field("companyName") companyName: String, @Field("basicUnitPrice") basicUnitPrice: Double,
                    @Field("soldCount") soldCount: Int,@Field("limitTime") limitTime: Long,
                    @Field("notes") notes: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/getProductInfo")
    fun getProductInfo(@Field("token") token: String, @Field("productId") productId: Long): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/checkSharesCode")
    fun getSharesInfo(@Field("token") token: String, @Field("sharesCode") sharesCode: String): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/attention")
    fun followProduct(@Field("token") token: String, @Field("productId")productId: Long , @Field("cancel") cancel: Int): Observable<JsonNode>



    @FormUrlEncoded
    @POST("products/attention")
    fun addBidIntention(@Field("token") token: String, @Field("productId")productId: Long): Observable<JsonNode>


    @FormUrlEncoded
    @POST("products/productBids")
    fun fetchBidList(@Field("token") token: String, @Field("productId")productId: Long, @Field("size")size: Int = 3): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/bid")
    fun pushBid(@Field("token") token: String, @Field("productId")productId: Long, @Field("money")size: Double, @Field("count")count: Long): Observable<JsonNode>

    @FormUrlEncoded
    @POST("products/removebid")
    fun removeBid(@Field("token") token: String, @Field("bidId")bidId: Long): Observable<JsonNode>
}