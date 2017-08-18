package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.api.ProductApi
import io.reactivex.Observable

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductRepository {

    fun getProductList(token: String?, pageNumber: Int, checkStatus: String, order: String = ""): Observable<MutableList<Product>> {
        return API.appClient.create(ProductApi::class.java).getProductList(token, pageNumber, checkStatus, order)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){}) }
    }

    fun pushProduct(token: String, sharesCodes: String, companyName: String,  basicUnitPrice: Double,
                    soldCount: Int, limitTime: Long, notes: String): Observable<Product>{

        return API.appClient.create(ProductApi::class.java).pushProduct(token, sharesCodes, companyName, basicUnitPrice, soldCount, limitTime, notes)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<Product>(data, Product::class.java) }
    }

    fun getProductInfo(token: String, productId: Long): Observable<Product> {
        return API.appClient.create(ProductApi::class.java).getProductInfo(token, productId)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<Product>(data, Product::class.java) }
    }

    fun getSharesInfo(token: String, sharesCodes: String): Observable<SharesInfo> {
        return API.appClient.create(ProductApi::class.java).getSharesInfo(token, sharesCodes)
                .map {response -> API.parseResponse(response) }
                .map {data ->API.objectMapper.readValue<SharesInfo>(data, SharesInfo::class.java) }
    }

    fun followProduct(token: String, productId: Long): Observable<String>{
        return API.appClient.create(ProductApi::class.java).followProduct(token, productId)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }
}