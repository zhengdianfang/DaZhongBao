package com.zhengdianfang.dazhongbao.views.product


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.PixelUtils
import com.zhengdianfang.dazhongbao.helpers.SpannableStringUtils
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class BidIntentionsFragment : BaseFragment() {

    private val product by lazy { API.objectMapper.readValue(arguments?.getString("product"), Product::class.java) }
    private val basicPriceView by lazy { view?.findViewById<TextView>(R.id.basicPriceView)!! }
    private val soldCountView by lazy { view?.findViewById<TextView>(R.id.soldCountView)!! }
    private val totalPriceView by lazy { view?.findViewById<TextView>(R.id.totalPriceView)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_bid_intentions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (null != product){
            val basicPriceString = getString(R.string.start_auction_price, getString(R.string.price_unit_value, product.basicUnitPrice.toString()))
            val highlightColor = ContextCompat.getColor(context, R.color.c_f43d3d)
            basicPriceView.text = SpannableStringUtils.addColorSpan(basicPriceString, product.basicUnitPrice.toString(), highlightColor, PixelUtils.sp2px(context, 16f).toInt())
            val soldCountString = getString(R.string.product_item_to_sell, getString(R.string.sold_count_value, product.soldCount.toString()))
            soldCountView.text = SpannableStringUtils.addColorSpan(soldCountString, getString(R.string.sold_count_value, product.soldCount.toString()), highlightColor, PixelUtils.sp2px(context, 16f).toInt())

            val totalPriceString = getString(R.string.total_price_label, 0.toString())
        }
    }

}// Required empty public constructor
