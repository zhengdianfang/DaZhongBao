package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 14/08/2017.
 */
class ProductDetailPresenter: BasePresenter() {
    private val productRepository by lazy { ProductRepository() }

    fun fetchProductInfo(productId: Long) {
        val loginUser = CApplication.INSTANCE.loginUser
        mView?.showLoadingDialog()
        addSubscription(productRepository.getProductInfo(loginUser?.token ?: "", productId),
                Consumer { product ->
                    (mView as IProductInfoView).renderProductInfo(product)
                    switchActionBarButtonStyle(product)
                    mView?.hideLoadingDialog()
                })
    }


    fun addBidIntention(productId: Long) {
        val loginUser = CApplication.INSTANCE.loginUser
        mView?.showLoadingDialog()
        addSubscription(productRepository.addBidIntention(loginUser?.token ?: "", productId),
                Consumer { msg ->
                    (mView as IProductInfoView).bidIntentionSuccess(msg)
                    mView?.hideLoadingDialog()
                    fetchProductInfo(productId)
                })
    }

    fun fetchProductInfoAndBidList(productId: Long){
        val loginUser = CApplication.INSTANCE.loginUser!!
        mView?.showLoadingDialog()
        val requestObservable = Observable.zip(productRepository.getProductInfo(loginUser.token!!, productId), productRepository.fetchBidList(loginUser.token!! , productId)
                , BiFunction<Product, MutableList<Bid>, Pair<Product, MutableList<Bid>>> { product, list-> Pair(product, list)})
        addSubscription(requestObservable, Consumer{ (first, second) ->
                    (mView as IProductInfoView).renderProductInfo(first)
                    (mView as IProductInfoView).renderBidList(second)
                    switchActionBarButtonStyle(first)
                    mView?.hideLoadingDialog()
                })

    }

    private fun switchActionBarButtonStyle(product: Product) {
        when(product?.check_status){
            0,6,7 ->{
                (mView as IProductInfoView).renderActionBar(R.color.activity_login_weixin_button_text_color, R.string.product_status_complete, 0, null)
            }
            1 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_f43d3d, R.string.product_status_verify, R.string.product_status_verify, null)
            }
            3 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_f9b416, R.string.product_status_itention, R.string.product_status_intention_info, {
                    addBidIntention(product.id)
                })
            }
            4 ->{
                if(product.bond_status != 2){
                    (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_margin, R.string.product_status_waiting_start_info, null)
                }else{
                    (mView as IProductInfoView).renderActionBar(R.color.colorPrimary, R.string.product_status_waiting_start, R.string.product_status_waiting_start_info, null)
                }
            }
            5 ->{
                if(product?.bidCount == 3) {
                    (mView as IProductInfoView).renderActionBar(R.color.colorPrimary, R.string.product_status_auctioning, R.string.product_status_auctioning_no_bid, null)
                }else{
                    (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_bid, 0, null)
                }
            }
        }
    }


    interface IProductInfoView: IView {
        fun renderProductInfo(product: Product)
        fun renderActionBar(backgroundColorResId: Int, textResId: Int, statusInfoStringResId: Int, onClick: (()-> Unit)?)
        fun bidIntentionSuccess(msg: String)
        fun renderBidList(list: MutableList<Bid>)
    }
}