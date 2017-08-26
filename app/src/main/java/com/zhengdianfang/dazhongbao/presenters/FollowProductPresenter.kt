package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.RxBus
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
            (mView as IFollowProductView).followSuccess(msg)
            RxBus.instance.post(Action(Action.FOLLOW_PRODUCT_ACTION, productId))
        })
    }

    fun unfollowProduct(token: String, productId: Long) {
        mView?.showLoadingDialog()
        addSubscription(productRepository.followProduct(token, productId, 1), Consumer {msg ->
            mView?.hideLoadingDialog()
            (mView as IFollowProductView).unfollowSuccess(msg)
            RxBus.instance.post(Action(Action.CANCEL_FOLLOW_PRODUCT_ACTION, productId))
        })
    }


    interface IFollowProductView: IView{
        fun followSuccess(msg: String)
        fun unfollowSuccess(msg: String)
    }

}

