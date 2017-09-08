package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 21/08/2017.
 */
class AuctionPresenter: BasePresenter() {

    private val productRepository by lazy { ProductRepository() }

    fun fetchAuctionList(token:String,number: Int) {
        addSubscription(productRepository.getProductList(token, number,  "5", "-startDateTime"), Consumer {list->
            (mView as IAuctionListView).receiveAuctionProductList(list.filter { (it.check_status == 4 || it.check_status == 5) } as MutableList<Product>)
        })
    }

    interface IAuctionListView: IView {
        fun receiveAuctionProductList(list: MutableList<Product>)
    }
}