package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.api.*
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

    fun getProductList(token: String?, pageNumber: Int, checkStatus: String, order: String = ""): Observable<Result<MutableList<Product>>> {
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS).map { list -> Result(list) }
        }
        return API.appClient.create(ProductApi::class.java).getProductList(token, pageNumber, checkStatus, order)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){}) }
                .map { list -> Result(list) }
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

    fun followProduct(token: String, productId: Long, cancel: Int = 0): Observable<String>{
        if (MOCK){
            return Observable.just(if(cancel == 0)"注关成功" else "取消成功成功").delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(ProductApi::class.java).followProduct(token, productId, cancel)
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

    fun payDeposit(token: String, productId: Long, money: Double): Observable<AlipayResult> {
        return API.appClient.create(ProductApi::class.java).payDeposit(token, productId, money)
                .map {response ->
                    if (response.has("errCode") && response.get("errCode").asInt() != 0) {
                        throw CException(response.get("msg").asText(), response.get("errCode").asInt())
                    }
                    API.objectMapper.readValue(response.toString(), AlipayResult::class.java)
                }
    }


    fun bondPayed(token: String, productId: Long, paykey: String, trade_no: String, out_trade_no: String ): Observable<String> {
        return API.appClient.create(ProductApi::class.java).bondPayed(token, productId, paykey, trade_no, out_trade_no)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }
}