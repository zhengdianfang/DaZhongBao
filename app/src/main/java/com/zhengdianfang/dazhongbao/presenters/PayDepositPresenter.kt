package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.product.AlipayResult
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.presenters.validates.UserInfoInterityValidate
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 27/08/2017.
 */
class PayDepositPresenter: BasePresenter() {
    private val productRepository = ProductRepository()
    private val validate  by lazy { UserInfoInterityValidate(mView) }

    fun payDeposit(token: String, productId: Long, money: Double){
        if (validate.validate()) {
            mView?.showLoadingDialog()
            addSubscription(productRepository.payDeposit(token, productId, money), Consumer {result ->
                (mView as IPayDepositResultView).payResult(result)
                mView?.hideLoadingDialog()
            })
        }
    }

    fun bondPayed(token: String, productId: Long, paykey: String, payResult: Map<String, String>) {
        if (validate.checkLogin()) {
            mView?.showLoadingDialog()
            val resultJson = API.objectMapper.readTree(payResult.get("result"))
            val trade_no = resultJson.get("alipay_trade_app_pay_response").get("trade_no").asText()
            val out_trade_no = resultJson.get("alipay_trade_app_pay_response").get("out_trade_no").asText()
            addSubscription(productRepository.bondPayed(token, productId, paykey, trade_no, out_trade_no), Consumer {result ->
                (mView as IPayDepositResultView).notifyBackendResult(result)
                mView?.hideLoadingDialog()
            })
        }
    }

    interface IPayDepositResultView: IView {
        fun payResult(alipayResult: AlipayResult)
        fun notifyBackendResult(result: String)
    }
}