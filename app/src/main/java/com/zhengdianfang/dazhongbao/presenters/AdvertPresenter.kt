package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.models.login.UserRepository
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 20/08/2017.
 */
class AdvertPresenter: BasePresenter() {
    private val productRepository by lazy { ProductRepository() }
    private val userRepository by lazy { UserRepository() }

    fun fetchAdvertBanner(token: String) {
        addSubscription(productRepository.fetchAdvertList(token), Consumer {list ->
            (mView as IAdvertBannerView).receiveBanner(list)
        })
    }

    fun fetchIndexCount(token: String) {
        addSubscription(userRepository.fetchIndexCount(token), Consumer {intArray->
            (mView as IIndexCountView).receiveIndexCount(intArray[0], intArray[1], intArray[2])
        })
    }

    interface IAdvertBannerView: IView{
        fun receiveBanner(advertList: MutableList<Advert>)
    }

    interface  IIndexCountView: IView{
        fun receiveIndexCount(dealCount: Int, productCount: Int, messageCount: Int)
    }
}