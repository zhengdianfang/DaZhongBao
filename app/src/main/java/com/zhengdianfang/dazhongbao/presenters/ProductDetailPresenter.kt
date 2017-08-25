package com.zhengdianfang.dazhongbao.presenters

import android.content.Context
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import java.text.SimpleDateFormat
import java.util.*

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
    data class ButtonStyle(var textResId: Int, var backgroundColorId: Int)
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
                notes = context.getString(R.string.product_status_waiting_start_info, SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(product.startDateTime)) )
            }
            PAY_BOND_BUTTON_TYPE -> {
                notes = context.getString(R.string.product_status_waiting_start_info)
            }
            AUCTIONING_BUTTON_NO_BOND_TYPE->{
                notes = context.getString(R.string.product_status_auctioning_no_bid)
            }
            SUMBIT_ATTETION_BUTTON_TYPE ->{
                notes = context.getString(R.string.product_status_waiting_start_info, SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Date(product.startDateTime)) )
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
                if (product.attention == 1){
                    type = MAKE_AUCTION_TIME_BUTTON_TYPE //排期中
                }else{
                    type = SUMBIT_ATTETION_BUTTON_TYPE //提交意向
                }
            }
            4 ->{
                if(product.bond_status != 2){
                    type = PAY_BOND_BUTTON_TYPE //提交保证金
                }else {
                    type = WATTING_AUCTION_START_BUTTON_TYPE //等待开拍
                }
            }
            5 ->{
                if(product?.bond_status != 2) {
                    type = AUCTIONING_BUTTON_NO_BOND_TYPE //竞拍中， 没交保证金
                }else{
                    type = AUCTIONING_BUTTON_TYPE //出价
                }
            }
        }

        return type
    }

    fun getStatusViewStyle(product: Product): ButtonStyle {
        var buttonStyle = ButtonStyle(R.string.product_status_complete, R.color.activity_login_weixin_button_text_color)
        when(getStatusViewType(product)){
            FINISH_BUTTON_TYPE ->{
               buttonStyle = ButtonStyle(R.string.product_status_complete, R.color.activity_login_weixin_button_text_color)
            }
            CHECKING_BUTTON_TYPE ->{
                buttonStyle = ButtonStyle(R.string.product_status_verify, R.color.c_f43d3d)
            }
            SUMBIT_ATTETION_BUTTON_TYPE  ->{
                buttonStyle = ButtonStyle(R.string.product_status_itention, R.color.c_f9b416)
            }
            PAY_BOND_BUTTON_TYPE ->{
                buttonStyle = ButtonStyle(R.string.product_status_margin, R.color.c_3cc751)
            }
            WATTING_AUCTION_START_BUTTON_TYPE -> {
                buttonStyle = ButtonStyle(R.string.product_status_waiting_start, R.color.colorPrimary)
            }
            AUCTIONING_BUTTON_TYPE->{
                buttonStyle = ButtonStyle(R.string.product_status_bid, R.color.c_3cc751)
            }
            AUCTIONING_BUTTON_NO_BOND_TYPE -> {
                buttonStyle = ButtonStyle(R.string.product_status_auctioning, R.color.colorPrimary)
            }
            MAKE_AUCTION_TIME_BUTTON_TYPE -> {
                buttonStyle = ButtonStyle(R.string.product_status_itention_info, R.color.colorPrimary)
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