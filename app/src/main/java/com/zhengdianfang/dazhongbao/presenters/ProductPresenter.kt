package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.presenters.validates.PushProductValidate
import com.zhengdianfang.dazhongbao.views.product.IProductList
import com.zhengdianfang.dazhongbao.views.product.IPushProduct
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 10/08/2017.
 */
class ProductPresenter: BasePresenter() {

    val productRepository by lazy { ProductRepository() }
    val mPushProductValidate by lazy { PushProductValidate(mView) }

    fun fetchProductList(token: String? = "", pageNumber: Int = 0, checkStatus: Int = -1) {
        mView?.showLoadingDialog()
        addSubscription(productRepository.getProductList(token, pageNumber, checkStatus), Consumer<MutableList<Product>> {products ->
            mView?.hideLoadingDialog()
            (mView as IProductList).receiverProductList(products)
        })
    }

    fun pushProductBeforeValidate(sharesCodes: String, companyName: String,  basicUnitPrice: Double, soldCount: Int): Boolean {
       return mPushProductValidate.checkLogin() && mPushProductValidate.validateFields(sharesCodes, companyName, basicUnitPrice, soldCount)
    }

    fun pushProduct(token: String, sharesCodes: String, companyName: String,  basicUnitPrice: Double,
                    soldCount: Int, limitTime: Long, notes: String) {

        mView?.showLoadingDialog()
        addSubscription(productRepository.pushProduct(token, sharesCodes, companyName, basicUnitPrice, soldCount, limitTime, notes), Consumer<Product> {product ->
            mView?.hideLoadingDialog()
            (mView as IPushProduct).receiverProduct(product)
        })
    }
}