package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.AdvertApi
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.api.ProductApi
import com.zhengdianfang.dazhongbao.models.mock.mockBid
import com.zhengdianfang.dazhongbao.models.mock.mockBidList
import com.zhengdianfang.dazhongbao.models.mock.mockProduct
import com.zhengdianfang.dazhongbao.models.mock.mockUserProducts
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductRepository(private val MOCK :Boolean = Constants.MOCK) {

    fun getProductList(token: String?, pageNumber: Int, checkStatus: String, order: String = ""): Observable<MutableList<Product>> {
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS)
        }
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
        if (MOCK){
            return Observable.just(mockProduct).delay(2, TimeUnit.SECONDS)
        }
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
        if (MOCK){
            return Observable.just("注关成功").delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(ProductApi::class.java).followProduct(token, productId)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }

    fun addBidIntention(token: String, productId: Long ): Observable<String> {
        return API.appClient.create(ProductApi::class.java).addBidIntention(token, productId)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }

    }

    fun fetchBidList(token: String, productId: Long): Observable<MutableList<Bid>> {
        if (MOCK){
            return Observable.just(mockBidList).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(ProductApi::class.java).fetchBidList(token, productId)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Bid>>(data, object : TypeReference<MutableList<Bid>>() { }) }
    }

    fun fetchAdvertList(token: String): Observable<MutableList<Advert>> {
        return API.appClient.create(AdvertApi::class.java).fetchAdvertBanner(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Advert>>(data, object : TypeReference<MutableList<Advert>>() { }) }
    }

    fun pushBid(token: String,productId: Long, price: Double, count: Long): Observable<Bid>{
        if (MOCK){
            return Observable.just(mockBid).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(ProductApi::class.java).pushBid(token, productId,price, count)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<Bid>(data, Bid::class.java) }
    }

    fun removeBid(token: String, bidId: Long): Observable<String>{
        if (MOCK){
            return Observable.just("取消成功").delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(ProductApi::class.java).removeBid(token, bidId)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }
}