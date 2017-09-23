package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.product.ProductCacheRepository
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.product.IProductList
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductPresenter: BasePresenter() {

    private val productRepository by lazy { ProductRepository() }
    private val productCacheRepository by lazy { ProductCacheRepository(CApplication.INSTANCE.memoryCache, CApplication.INSTANCE.diskCahce) }

    fun fetchProductList(token: String? = "", pageNumber: Int = 0, checkStatus: String= "", order: String = "") {
        var observable = productRepository.getProductList(token, pageNumber, checkStatus, order)
        if (pageNumber < 2){
            observable = Observable.concat(
                    productCacheRepository.loadHomeProductsCache(checkStatus),
                    productRepository.getProductList(token, pageNumber, checkStatus, order)
                            .delay(1000, TimeUnit.MILLISECONDS)
                            .doOnNext { result ->
                                if (!result.isCache && pageNumber < 2){
                                    productCacheRepository.saveHomeProductsCache(checkStatus, result.data)
                                }
                            }
            )
        }
        addSubscription(observable, Consumer{result ->
            (mView as IProductList).receiverProductList(result.data, result.isCache)
        })
    }
}