package com.zhengdianfang.dazhongbao.presenters

import android.content.Context
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.basic.IView
import com.zhengdianfang.dazhongbao.views.product.CreateBidFragment
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 14/08/2017.
 */
class ProductDetailPresenter: BasePresenter() {
    companion object {
        val FINISH_BUTTON_TYPE = 0
        val CHECKING_BUTTON_TYPE = 1
        val MAKE_AUCTION_TIME_BUTTON_TYPE = 2
        val PAY_BOND_BUTTON_TYPE = 3
        val WATTING_AUCTION_START_BUTTON_TYPE = 4
        val AUCTIONING_BUTTON_TYPE = 5
        val SUMBIT_ATTETION_BUTTON_TYPE = 6
        val AUCTIONING_BUTTON_NO_BOND_TYPE = 7
    }
    data class ButtonStyle(var textResId: Int, var backgroundColorId: Int, var onClick: (() -> Unit)?)
    private val productRepository by lazy { ProductRepository() }

    private fun fetchProductInfo(productId: Long) {
        val loginUser = CApplication.INSTANCE.loginUser
        mView?.showLoadingDialog()
        addSubscription(productRepository.getProductInfo(loginUser?.token ?: "", productId),
                Consumer { product ->
                    (mView as IProductInfoView).renderProductInfo(product)
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
        val requestObservable = Observable.zip(productRepository.getProductInfo(loginUser.token!!, productId), productRepository.fetchBidList(loginUser.token!! , productId)
                , BiFunction<Product, MutableList<Bid>, Pair<Product, MutableList<Bid>>> { product, list-> Pair(product, list)})
        addSubscription(requestObservable, Consumer{ (first, second) ->
                    (mView as IProductInfoView).renderProductInfo(first)
                    (mView as IProductInfoView).renderBidList(second)
                })

    }

    fun getStatusNoteString(context: Context, product: Product): String {
        var notes = ""
        when(getStatusViewType(product)){
            CHECKING_BUTTON_TYPE ->{
                notes = context.getString(R.string.product_status_verify)
            }
            MAKE_AUCTION_TIME_BUTTON_TYPE ->{
                notes = context.getString(R.string.product_status_intention_info)
            }
            WATTING_AUCTION_START_BUTTON_TYPE ->{
                notes = context.getString(R.string.product_status_waiting_start_info, DateUtils.formatTime(product.startDateTime))
            }
            PAY_BOND_BUTTON_TYPE -> {
                notes = context.getString(R.string.product_status_waiting_start_info, DateUtils.formatTime(product.startDateTime))
            }
            AUCTIONING_BUTTON_NO_BOND_TYPE->{
                notes = context.getString(R.string.product_status_auctioning_no_bid)
            }
            SUMBIT_ATTETION_BUTTON_TYPE ->{
                notes = context.getString(R.string.product_status_waiting_start_info, DateUtils.formatTime(product.startDateTime))
            }
        }
        return notes
    }

    fun getStatusViewType(product: Product): Int {
        var type = FINISH_BUTTON_TYPE
        when(product?.check_status){
            0,6,7 ->{
               type = FINISH_BUTTON_TYPE //已结束
            }
            1 ->{
                type = CHECKING_BUTTON_TYPE //审核中
            }
            3 ->{
                type = if(product.isSeller()){
                    PAY_BOND_BUTTON_TYPE //提交保证金
                }else {
                    if (product.attention == 1){
                        MAKE_AUCTION_TIME_BUTTON_TYPE //排期中
                    }else{
                        SUMBIT_ATTETION_BUTTON_TYPE //提交意向
                    }
                }
            }
            4 ->{
                if (!product.isSeller()){
                    type = if(product.bond_status != 2){
                        PAY_BOND_BUTTON_TYPE //提交保证金
                    }else {
                        WATTING_AUCTION_START_BUTTON_TYPE //等待开拍
                    }
                }
            }
            5 ->{
                if (!product.isSeller()){
                    type = if(product?.bond_status != 2) {
                        AUCTIONING_BUTTON_NO_BOND_TYPE //竞拍中， 没交保证金
                    }else{
                        AUCTIONING_BUTTON_TYPE //出价
                    }
                }
            }
        }

        return type
    }

    fun getStatusViewStyle(activity: BaseActivity, product: Product): ButtonStyle {
        var buttonStyle = ButtonStyle(R.string.product_status_complete, R.color.activity_login_weixin_button_text_color, null)
        when(getStatusViewType(product)){
            FINISH_BUTTON_TYPE ->{
               buttonStyle = ButtonStyle(R.string.product_status_complete, R.color.activity_login_weixin_button_text_color, null)
            }
            CHECKING_BUTTON_TYPE ->{
                buttonStyle = ButtonStyle(R.string.product_status_verify, R.color.activity_login_weixin_button_text_color, null)
            }
            SUMBIT_ATTETION_BUTTON_TYPE  ->{
                buttonStyle = ButtonStyle(R.string.product_status_itention, R.color.c_f9b416, null)
            }
            PAY_BOND_BUTTON_TYPE ->{
                buttonStyle = ButtonStyle(R.string.product_status_margin, R.color.c_3cc751, null)
            }
            WATTING_AUCTION_START_BUTTON_TYPE -> {
                buttonStyle = ButtonStyle(R.string.product_status_waiting_start, R.color.colorPrimary, null)
            }
            AUCTIONING_BUTTON_TYPE->{
                buttonStyle = ButtonStyle(R.string.product_status_bid, R.color.c_3cc751, {
                    val fragment = CreateBidFragment()
                    fragment.product = product
                   activity.startFragment(android.R.id.content, fragment, "pay")
                })
            }
            AUCTIONING_BUTTON_NO_BOND_TYPE -> {
                buttonStyle = ButtonStyle(R.string.product_status_auctioning, R.color.activity_login_weixin_button_text_color, null)
            }
            MAKE_AUCTION_TIME_BUTTON_TYPE -> {
                buttonStyle = ButtonStyle(R.string.product_status_itention_info, R.color.activity_login_weixin_button_text_color, null)
            }
        }
        return buttonStyle
    }


    interface IProductInfoView: IView {
        fun renderProductInfo(product: Product)
        fun bidIntentionSuccess(msg: String)
        fun renderBidList(list: MutableList<Bid>)
    }

}