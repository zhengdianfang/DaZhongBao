package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.ProductApi
import io.reactivex.Observable

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductRepository {

    fun getProductList(token: String?, pageNumber: Int, checkStatus: Int): Observable<MutableList<Product>> {
        return API.appClient.create(ProductApi::class.java).getProductList(token, pageNumber, checkStatus)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){}) }
    }

    fun pushProduct(token: String, sharesCodes: String, companyName: String,  basicUnitPrice: Double,
                    soldCount: Int, limitTime: Long, notes: String): Observable<Product>{

        return API.appClient.create(ProductApi::class.java).pushProduct(token, sharesCodes, companyName, basicUnitPrice, soldCount, limitTime, notes)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<Product>(data, Product::class.java) }
    }
}