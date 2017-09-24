package com.zhengdianfang.dazhongbao.views.product


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.content.ContextCompat
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.*
import com.zhengdianfang.dazhongbao.models.product.Bid
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.PushBidPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * A simple [Fragment] subclass.
 */
class BidIntentionsFragment : BaseFragment() , PushBidPresenter.IPushBidView , BasePresenter.ICheckUserIntegrityView{

    private val basicPriceView by lazy { view?.findViewById<TextView>(R.id.basicPriceView)!! }
    private val soldCountView by lazy { view?.findViewById<TextView>(R.id.soldCountView)!! }
    private val totalPriceView by lazy { view?.findViewById<TextView>(R.id.totalPriceView)!! }
    private val licenseTextView by lazy { view?.findViewById<TextView>(R.id.licenseTextView)!! }
    private val submitButton by lazy { view?.findViewById<Button>(R.id.submitButton)!! }
    private val pushBidPresenter = PushBidPresenter()
    private val successDialog by lazy { MaterialDialog.Builder(context).customView(R.layout.dialog_create_bid_success_layout, false).build() }


    var payCount = 0L
    var payPrice = 0.0
    var product:Product? = null


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_bid_intentions, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pushBidPresenter.attachView(this)
        if (null != product){
            basicPriceView.text = ViewsUtils.renderSharesPrice(context, payPrice, R.string.starting_price_label, 20f)
            soldCountView.text = ViewsUtils.renderSharesSoldCount(context, payCount, 20f)
            totalPriceView.text = ViewsUtils.renderSharesPrice(context, payCount * payPrice, R.string.total_price_label, 20f)
            licenseTextView.text = Html.fromHtml(FileUtils.readRawFile(context, R.raw.bid_license))

            submitButton.setOnClickListener {
                pushBidPresenter.pushBid(CApplication.INSTANCE.loginUser?.token!!, product!!,  payPrice, payCount)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        pushBidPresenter.detachView()
    }

    override fun pushBidSuccess(bid: Bid) {
        val contentString = getString(R.string.now_top_price, "￥${bid.highest}")
        successDialog.customView?.findViewById<TextView>(R.id.nowTopPriceView)?.text = SpannableStringUtils.addColorSpan(contentString, "￥${bid.highest}",
                ContextCompat.getColor(context, R.color.colorPrimary), PixelUtils.sp2px(context, 24f).toInt())
        successDialog.show()
        Observable.just(1).delay(3, TimeUnit.SECONDS).subscribe { successDialog.cancel() }
        getParentActivity().supportFragmentManager.popBackStack("bid", FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
    override fun notIntegrity(type: Int) {
       AppUtils.interityUserInfo(getParentActivity(), type)
    }
}// Required empty public constructor
