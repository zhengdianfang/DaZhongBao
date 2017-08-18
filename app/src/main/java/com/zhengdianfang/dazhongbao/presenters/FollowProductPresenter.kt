package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 18/08/2017.
 */
class FollowProductPresenter: BasePresenter() {

    private val productRepository = ProductRepository()

    fun followProduct(token: String, productId: Long) {
       mView?.showLoadingDialog()
        addSubscription(productRepository.followProduct(token, productId), Consumer {msg ->
            mView?.hideLoadingDialog()
            (mView as IFollowProductView).followSuccess(msg, productId)
        })
    }

    interface IFollowProductView : IView{
       fun followSuccess(msg: String, productId: Long)
    }
}