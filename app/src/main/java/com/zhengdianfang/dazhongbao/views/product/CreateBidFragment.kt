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
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.PushBidPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pushBidPresenter.detachView()
    }

    private fun renderViews() {
       if (product != null) {
           nowTopPriceView.text = "￥${product?.nowUnitPrice}"
           bidPriceView.addTextChangedListener(object: TextWatcher{
               override fun afterTextChanged(editable: Editable?) {
                   val price = editable?.toString()!!.toInt()
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
                   val count = editable?.toString()!!.toLong()
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
        cancelBidViewGroup.removeAllViews()
        this.product?.mybids?.forEachIndexed { index, bid ->
            val itemView = LayoutInflater.from(context).inflate(R.layout.cancel_bond_price_item_layout, null, false)
            itemView.findViewById<TextView>(R.id.dealUnitPriceView).text = bid.price.toString() + getString(R.string.fragment_push_price_unit)
            itemView.findViewById<TextView>(R.id.dealCountView).text = getString(R.string.sold_count_value,
                    (bid.count/ Constants.SOLD_COUNT_BASE_UNIT).toString()) + getString(R.string.stock_unit)
            itemView.findViewById<TextView>(R.id.dealTotlaPriceView).text =
                    getString(R.string.total_price_label, getString(R.string.sold_count_value, (bid.count * bid.price / Constants.SOLD_COUNT_BASE_UNIT).toInt().toString()))
            itemView.findViewById<Button>(R.id.removeButton).setOnClickListener {
                pushBidPresenter.removeBid(CApplication.INSTANCE.loginUser?.token!!, bid.id)
            }
            cancelBidViewGroup.addView(itemView)
        }
    }

    override fun pushBidSuccess(bid: Bid) {
        this.product?.mybids?.add(bid)
        renderMyBidList()
        toast(R.string.push_bid_success)
    }

    override fun removeBidSuccess(bidId: Long, msg: String) {
        if (null != product){
            val removeItem = this.product!!.mybids.filter { it.id == bidId }
            this.product?.mybids?.remove(removeItem.first())
            renderMyBidList()
            toast(msg)
        }
    }

}// Required empty public constructor
