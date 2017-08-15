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
                Consumer<Product> { product ->
                    (mView as IProductInfoView).renderProductInfo(product)
                    switchActionBarButtonStyle(product)
                    mView?.hideLoadingDialog()
                })
    }

    fun switchActionBarButtonStyle(product: Product) {
        when(product?.check_status){
            0,7,8 ->{
                (mView as IProductInfoView).renderActionBar(R.color.activity_login_weixin_button_text_color, R.string.product_status_complete)
            }
            1 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_f43d3d, R.string.product_status_verify)
            }
            3 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_margin)
            }
            4 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_f9b416, R.string.product_status_itention)
            }
            5 ->{
                (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_margin)
                (mView as IProductInfoView).renderActionBar(R.color.colorPrimary, R.string.product_status_waiting_start)
            }
            6 ->{
                if(product?.bidCount == 3) {
                    (mView as IProductInfoView).renderActionBar(R.color.colorPrimary, R.string.product_status_auctioning)
                }else{
                    (mView as IProductInfoView).renderActionBar(R.color.c_3cc751, R.string.product_status_bid)
                }
            }
        }
    }


    interface IProductInfoView: IView {
        fun renderProductInfo(product: Product)
        fun renderActionBar(backgroundColorResId: Int, textResId: Int)
    }
}