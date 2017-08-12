package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.product.IProductList
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductPresenter: BasePresenter() {

    val productRepository by lazy { ProductRepository() }

    fun fetchProductList(token: String? = "", pageNumber: Int = 0, checkStatus: Int = -1) {
        mView?.showLoadingDialog()
        addSubscription(productRepository.getProductList(token, pageNumber, checkStatus), Consumer<MutableList<Product>> {products ->
            mView?.hideLoadingDialog()
            (mView as IProductList).receiverProductList(products)
        })
    }
}