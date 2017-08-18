package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.models.product.SharesInfo
import com.zhengdianfang.dazhongbao.presenters.validates.PushProductValidate
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 16/08/2017.
 */
class PushProductPresenter: BasePresenter() {

    private val productRepository by lazy { ProductRepository() }
    private val mPushProductValidate by lazy { PushProductValidate(mView) }
    private val deounceObservable = Observable.just(1).debounce(500, TimeUnit.MILLISECONDS)

    fun pushProductBeforeValidate(sharesCodes: String, companyName: String,  basicUnitPrice: Double, soldCount: Int): Boolean {
        return mPushProductValidate.checkLogin() && mPushProductValidate.validateFields(sharesCodes, companyName, basicUnitPrice, soldCount)
    }

    fun pushProduct(token: String, sharesCodes: String, companyName: String,  basicUnitPrice: Double,
                    soldCount: Int, limitTime: Long, notes: String) {

        mView?.showLoadingDialog()
        addSubscription(productRepository.pushProduct(token, sharesCodes, companyName, basicUnitPrice, soldCount, limitTime, notes), Consumer<Product> { product ->
            mView?.hideLoadingDialog()
            (mView as IPushProduct).receiverProduct(product)
        })
    }

    fun getSharesInfo(token: String, sharesCodes: String){
        addSubscription(deounceObservable.flatMap { productRepository.getSharesInfo(token, sharesCodes)} , Consumer<SharesInfo> { sharesInfo->
            (mView as IPushProduct).receiverSharesInfo(sharesInfo)
        })
    }

    interface IPushProduct: IView {
        fun receiverProduct(product: Product)
        fun receiverSharesInfo(sharesInfo: SharesInfo)
    }
}