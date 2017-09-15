package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.Result
import com.zhengdianfang.dazhongbao.models.cache.BaseMemoryCache
import com.zhengdianfang.dazhongbao.models.cache.BasicDiskCache
import io.reactivex.Observable

/**
 * Created by dfgzheng on 15/09/2017.
 */
class ProductCacheRepository(private val memoryCache: BaseMemoryCache, private val diskCache: BasicDiskCache)  {
    companion object {
        val AUCTION_PRODUCT_LIST_CACHE = "auction_product_list_cache"
        val HOME_PRODUCT_LIST_CACHE = "home_product_list_cache_%s"
    }
    fun loadAuctionProductsCache(): Observable<Result<MutableList<Product>>> {
        var observable = Observable.just(Result(mutableListOf<Product>()))
        val memory = memoryCache[AUCTION_PRODUCT_LIST_CACHE]
        val disk = diskCache.getFromCache(AUCTION_PRODUCT_LIST_CACHE)
        if (disk != null){
            observable = Observable.just(disk)
                    .map {cache -> API.objectMapper.readValue<MutableList<Product>>(cache.data, object : TypeReference<MutableList<Product>>(){})}
                    .map { list -> Result(list, true) }
        }
        if (memory != null){
            observable = Observable.just(memory.obj as MutableList<Product>)
                    .map { list -> Result(list, true) }
        }
        return observable
    }

    fun saveAuctionProductsCache(list: MutableList<Product>?){
        if (list != null) {
            memoryCache.add(AUCTION_PRODUCT_LIST_CACHE, list)
            diskCache.addInCache(AUCTION_PRODUCT_LIST_CACHE, API.objectMapper.writeValueAsBytes(list))
        }
    }


    fun loadHomeProductsCache(check_status: String): Observable<Result<MutableList<Product>>> {
        val key = String.format(HOME_PRODUCT_LIST_CACHE, check_status)
        var observable = Observable.just(Result(mutableListOf<Product>()))
        val memory = memoryCache[key]
        val disk = diskCache.getFromCache(key)
        if (disk != null){
            observable = Observable.just(disk)
                    .map {cache -> API.objectMapper.readValue<MutableList<Product>>(cache.data, object : TypeReference<MutableList<Product>>(){})}
                    .map { list -> Result(list, true) }
        }
        if (memory != null){
            observable = Observable.just(memory.obj as MutableList<Product>)
                    .map { list -> Result(list, true) }
        }
        return observable
    }

    fun saveHomeProductsCache(check_status: String, list: MutableList<Product>?){
        if (list != null) {
            val key = String.format(HOME_PRODUCT_LIST_CACHE, check_status)
            memoryCache.add(key, list)
            diskCache.addInCache(key, API.objectMapper.writeValueAsBytes(list))
        }
    }
}