package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
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

    fun switchActionBarButtonStyle(product: Product) {
        when(product?.check_status){
            0,6,7 ->{
                (mView as IProductInfoView).renderActionBar(R.color.activity_login_weixin_button_text_color, R.string.product_status_complete, 0)
            }
            1 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_f43d3d, R.string.product_status_verify, R.string.product_status_verify)
            }
            3 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_f9b416, R.string.product_status_itention, R.string.product_status_intention_info)
            }
            4 ->{
                if(product.bond == 0){
                    (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_margin, R.string.product_status_waiting_start_info)
                }else{
                    (mView as IProductInfoView).renderActionBar(R.color.colorPrimary, R.string.product_status_waiting_start, R.string.product_status_waiting_start_info)
                }
            }
            5 ->{
                if(product?.bidCount == 3) {
                    (mView as IProductInfoView).renderActionBar(R.color.colorPrimary, R.string.product_status_auctioning, R.string.product_status_auctioning_no_bid)
                }else{
                    (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_bid, 0)
                }
            }
        }
    }


    interface IProductInfoView: IView {
        fun renderProductInfo(product: Product)
        fun renderActionBar(backgroundColorResId: Int, textResId: Int, statusInfoStringResId: Int)
    }
}