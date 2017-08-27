package com.zhengdianfang.dazhongbao.views.product


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.RemoveBidResult
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.helpers.ViewsUtils
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.PushBidPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer


/**
 * A simple [Fragment] subclass.
 */
class CreateBidFragment : BaseFragment(), PushBidPresenter.IPushBidView , PushBidPresenter.IRemoveBidView{

    private val nowTopPriceView by lazy { view?.findViewById<TextView>(R.id.nowTopPriceView)!! }
    private val bidPriceView by lazy { view?.findViewById<EditText>(R.id.bidPriceView)!! }
    private val payCountView by lazy { view?.findViewById<EditText>(R.id.payCountView)!! }
    private val totalPriceView by lazy { view?.findViewById<TextView>(R.id.totalPriceView)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }
    private val cancelBidViewGroup by lazy {view?.findViewById<ViewGroup>(R.id.cancelBidViewGroup)!!}
    private val pushBidPresenter = PushBidPresenter()

    var product: Product? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_create_bid, container, false)
    }

    private var removeBidDisposable: Disposable? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pushBidPresenter.attachView(this)
        renderViews()
        renderMyBidList()
        submitButton.setOnClickListener {
            if(null != product) {
                pushBidPresenter.pushBid(CApplication.INSTANCE.loginUser?.token!!, product!!,  bidPriceView.text.toString(),
                        payCountView.text.toString())
            }
        }
        removeBidDisposable = RxBus.instance.register(Action.REMOVE_BID_ACTION, Consumer { (type, data) ->
            if (data is RemoveBidResult) {
                val removeItem = this.product!!.mybids?.filter { it.bidid == data.bidId }
                this.product?.mybids?.remove(removeItem?.first())
                renderMyBidList()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pushBidPresenter.detachView()
        RxBus.instance.unregister(removeBidDisposable)
    }

    private fun renderViews() {
       if (product != null) {
           nowTopPriceView.text = "￥${product?.nowUnitPrice}"
           bidPriceView.addTextChangedListener(object: TextWatcher{
               override fun afterTextChanged(editable: Editable?) {
                   val price = if(TextUtils.isEmpty(editable.toString())) 0 else editable.toString().toInt()
                   val count = if(TextUtils.isEmpty(payCountView.text.toString())) 0 else payCountView.text.toString().toInt()
                   totalPriceView.text = "￥${price * count}"
               }

               override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               }

               override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               }

           })
           payCountView.addTextChangedListener(object: TextWatcher{
               override fun afterTextChanged(editable: Editable?) {
                   val count = if(TextUtils.isEmpty(editable?.toString())) 0 else editable?.toString()!!.toLong()
                   val price = if(TextUtils.isEmpty(bidPriceView.text.toString())) 0 else bidPriceView.text.toString().toInt()
                   totalPriceView.text = "￥${price * count}"
               }

               override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               }

               override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               }

           })
       }
    }

    private fun renderMyBidList() {
        if(this.product?.mybids != null && this.product?.mybids!!.isNotEmpty()) {
            cancelBidViewGroup.visibility = View.VISIBLE
            cancelBidViewGroup.removeAllViews()
            ViewsUtils.renderBidListView(context, this.product, removeOnClick = {bid ->
                pushBidPresenter.removeBid(CApplication.INSTANCE.loginUser?.token!!, bid)
            }).forEach {
                cancelBidViewGroup.addView(it)
            }
        }else{
            cancelBidViewGroup.visibility = View.GONE
        }
    }

    override fun pushBidSuccess(bid: Bid) {
        this.product?.mybids?.add(bid)
        renderMyBidList()
        toast(R.string.push_bid_success)
    }

    override fun removeBidSuccess(msg: String) {
        if (null != product){
            toast(msg)
        }
    }

}// Required empty public constructor
