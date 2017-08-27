package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.AlipayResult
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.presenters.validates.BaseValidate
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 27/08/2017.
 */
class PayDepositPresenter: BasePresenter() {
    private val productRepository = ProductRepository()

    private val validate  = BaseValidate(mView)

    fun payDeposit(token: String, productId: Long, money: Double){
        if (validate.checkLogin()) {
            mView?.showLoadingDialog()
            addSubscription(productRepository.payDeposit(token, productId, money), Consumer {result ->
                (mView as IPayDepositResultView).payResult(result)
                mView?.hideLoadingDialog()
            })
        }
    }


    interface IPayDepositResultView: IView {
       fun payResult(alipayResult: AlipayResult)
    }
}