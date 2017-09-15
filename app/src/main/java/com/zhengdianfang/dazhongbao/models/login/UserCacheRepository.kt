package com.zhengdianfang.dazhongbao.models.login

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.Result
import com.zhengdianfang.dazhongbao.models.cache.BaseMemoryCache
import com.zhengdianfang.dazhongbao.models.cache.BasicDiskCache
import com.zhengdianfang.dazhongbao.models.product.Product
import io.reactivex.Observable

/**
 * Created by dfgzheng on 13/09/2017.
 */
class UserCacheRepository(private val memoryCache: BaseMemoryCache, private val diskCache: BasicDiskCache) {
    companion object {
        val MY_ATTENTION_PRODUCT_LIST_CACHE = "my_attention_product_list_cache"
        val MY_PRODUCT_LIST_CACHE = "my_product_list_cache"
        val MY_AUCTION_PRODUCT_LIST_CACHE = "my_auction_product_list_cache"
    }

    fun loadUserAttentionProductsCache(): Observable<Result<MutableList<Product>>> {
        var observable = Observable.just(Result(mutableListOf<Product>()))
        val memory = memoryCache[MY_ATTENTION_PRODUCT_LIST_CACHE]
        val disk = diskCache.getFromCache(MY_ATTENTION_PRODUCT_LIST_CACHE)
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

    fun saveUserAttentionProductsCache(list: MutableList<Product>?){
        if (list != null) {
            memoryCache.add(MY_ATTENTION_PRODUCT_LIST_CACHE, list)
            diskCache.addInCache(MY_ATTENTION_PRODUCT_LIST_CACHE, API.objectMapper.writeValueAsBytes(list))
        }
    }

    fun loadUserProductsCache(): Observable<Result<MutableList<Product>>> {
        var observable = Observable.just(Result(mutableListOf<Product>()))
        val memory = memoryCache[MY_PRODUCT_LIST_CACHE]
        val disk = diskCache.getFromCache(MY_PRODUCT_LIST_CACHE)
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

    fun saveUserProductsCache(list: MutableList<Product>?){
        if (list != null) {
            memoryCache.add(MY_PRODUCT_LIST_CACHE, list)
            diskCache.addInCache(MY_PRODUCT_LIST_CACHE, API.objectMapper.writeValueAsBytes(list))
        }
    }

    fun loadUserAuctionProductsCache(): Observable<Result<MutableList<Product>>> {
        var observable = Observable.just(Result(mutableListOf<Product>()))
        val memory = memoryCache[MY_AUCTION_PRODUCT_LIST_CACHE]
        val disk = diskCache.getFromCache(MY_AUCTION_PRODUCT_LIST_CACHE)
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

    fun saveUserAuctionProductsCache(list: MutableList<Product>?){
        if (list != null) {
            memoryCache.add(MY_AUCTION_PRODUCT_LIST_CACHE, list)
            diskCache.addInCache(MY_AUCTION_PRODUCT_LIST_CACHE, API.objectMapper.writeValueAsBytes(list))
        }
    }
}