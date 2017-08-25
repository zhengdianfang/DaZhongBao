package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.product.IProductList
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductPresenter: BasePresenter() {

    private val productRepository by lazy { ProductRepository() }

    fun fetchProductList(token: String? = "", pageNumber: Int = 0, checkStatus: String= "", order: String = "") {
        addSubscription(productRepository.getProductList(token, pageNumber, checkStatus, order), Consumer{products ->
            (mView as IProductList).receiverProductList(products)
        })
    }
}