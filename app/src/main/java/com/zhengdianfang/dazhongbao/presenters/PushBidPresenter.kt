package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.ProductRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 22/08/2017.
 */
class PushBidPresenter: BasePresenter() {

    private val productRepository  = ProductRepository()

    fun pushBid(token: String, product: Product, priceString: String, countString: String){
        val price = if(priceString.isEmpty())  0.0 else priceString.toDouble()
        val count = if(countString.isEmpty())  0 else countString.toLong()
        if (validatePriceAndCount(product, price, count)){
            mView?.showLoadingDialog()
            addSubscription(productRepository.pushBid(token, product.id, price, count), Consumer {newBid->
                mView?.hideLoadingDialog()
                (mView as IPushBidView).pushBidSuccess(newBid)

            })
        }
    }

    fun removeBid(token: String, bidId: Long){
        mView?.showLoadingDialog()
        addSubscription(productRepository.removeBid(token, bidId), Consumer {msg->
            mView?.hideLoadingDialog()
            (mView as IRemoveBidView).removeBidSuccess(bidId, msg)

        })
    }

    private fun validatePriceAndCount(product: Product, price: Double, count: Long): Boolean {
       var res = true
       if (price == 0.0){
           mView?.validateErrorUI(R.string.please_input_leggal_price)
           res = false
       }else if(count == 0L){
           mView?.validateErrorUI(R.string.please_input_leggal_count)
           res = false
       }else if (product.mybids?.count() == 3) {
           mView?.validateErrorUI(R.string.the_bid_max_count)
           res = false
       }
        return res
    }

    interface IPushBidView: IView {
       fun pushBidSuccess(bid: Bid)
    }
    interface IRemoveBidView: IView {
        fun removeBidSuccess(bidId: Long, msg: String)
    }
}