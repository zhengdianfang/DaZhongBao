package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.helpers.RemoveBidResult
import com.zhengdianfang.dazhongbao.helpers.RxBus
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

    fun pushBid(token: String, product: Product, price: Double, count: Long){
        mView?.showLoadingDialog()
        addSubscription(productRepository.pushBid(token, product.id, price, count), Consumer {newBid->
            mView?.hideLoadingDialog()
            (mView as IPushBidView).pushBidSuccess(newBid)
            RxBus.instance.post(Action(Action.ADD_BID_ACTION, newBid))

        })
    }

    fun removeBid(token: String, bid: Bid){
        mView?.showLoadingDialog()
        addSubscription(productRepository.removeBid(token, bid.bidId), Consumer {msg->
            mView?.hideLoadingDialog()
            (mView as IRemoveBidView).removeBidSuccess(msg)
            RxBus.instance.post(Action(Action.REMOVE_BID_ACTION, RemoveBidResult(bid.productId, bid.bidId)))

        })
    }

    fun validatePriceAndCount(product: Product, priceString: String, countString: String): Boolean {
        val price = if(priceString.isEmpty())  0.0 else priceString.toDouble()
        val count = if(countString.isEmpty())  0 else countString.toLong()
        var res = true
        when {
            price == 0.0 -> {
                mView?.validateErrorUI(R.string.please_input_leggal_price)
                res = false
            }
            count == 0L -> {
                mView?.validateErrorUI(R.string.please_input_leggal_count)
                res = false
            }
            product.mybids?.count() == 3 -> {
                mView?.validateErrorUI(R.string.the_bid_max_count)
                res = false
            }
            count * price < Constants.MIN_DEPOSIT_PRICE -> {
                mView?.validateErrorUI(R.string.bid_tip)
                res = false
            }
        }
        return res
    }

    interface IPushBidView: IView {
       fun pushBidSuccess(bid: Bid)
    }
    interface IRemoveBidView: IView {
        fun removeBidSuccess(msg: String)
    }
}