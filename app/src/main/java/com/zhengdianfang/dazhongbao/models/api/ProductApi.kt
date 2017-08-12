package com.zhengdianfang.dazhongbao.models.api

import com.fasterxml.jackson.databind.JsonNode
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by dfgzheng on 10/08/2017.
 */
interface ProductApi {

    @POST("products/plist")
    fun getProductList(@Query("token") token: String?, @Query("pageNumber") pageNumber: Int,
                       @Query("check_status") checkStatus: Int): Observable<JsonNode>
}