package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductCacheRepository
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 21/08/2017.
 */
class AuctionPresenter: BasePresenter() {

    private val productRepository by lazy { ProductRepository() }
    private val productCacheRepository by lazy { ProductCacheRepository(CApplication.INSTANCE.memoryCache, CApplication.INSTANCE.diskCahce) }

    fun fetchAuctionList(token:String,number: Int) {
        var observable = productRepository.getProductList(token, number,  "5", "-startDateTime")
        if (number < 2){
            observable = Observable.concat(
                    productCacheRepository.loadAuctionProductsCache(),
                    productRepository.getProductList(token, number,  "5", "-startDateTime").delay(300, TimeUnit.MILLISECONDS)
                            .doOnNext { result ->
                                if (!result.isCache && number < 2){
                                    productCacheRepository.saveAuctionProductsCache(result.data)
                                }
                            })
        }
        addSubscription(observable, Consumer {result->
            (mView as IAuctionListView).receiveAuctionProductList(
                    result.data.filter { (it.check_status == 4 || it.check_status == 5) } as MutableList<Product>, result.isCache)
        })
    }

    interface IAuctionListView: IView {
        fun receiveAuctionProductList(list: MutableList<Product>, isCache: Boolean)
    }
}