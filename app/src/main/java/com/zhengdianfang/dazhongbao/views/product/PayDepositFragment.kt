package com.zhengdianfang.dazhongbao.views.product


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.*
import com.zhengdianfang.dazhongbao.models.product.AlipayResult
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.PayDepositPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class PayDepositFragment : BaseFragment(), PayDepositPresenter.IPayDepositResultView , BasePresenter.ICheckUserIntegrityView{

    private val subButton by lazy { view?.findViewById<ImageButton>(R.id.subButton)!! }
    private val addButton by lazy { view?.findViewById<ImageButton>(R.id.addButton)!! }
    private val depositEditText by lazy { view?.findViewById<EditText>(R.id.depositEditText)!! }
    private val maxAuctionCountView by lazy { view?.findViewById<TextView>(R.id.maxAuctionCountView)!! }
    private val payButton by lazy { view?.findViewById<Button>(R.id.payButton)!! }
    var product: Product? = null
    private var depositCount = Constants.MIN_DEPOSIT_PRICE
    private val payDepositPresenter = PayDepositPresenter()
    private val alipayUtils by lazy { AliPayUtils(getParentActivity()) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pay_bond, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setStatusBarTheme(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, Color.WHITE)
        payDepositPresenter.attachView(this)
        setupBondViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        payDepositPresenter.detachView()
    }

    override fun toolbarBackButtonClick() {
        setStatusBarTheme(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR, ContextCompat.getColor(context, R.color.colorPrimary))
        getParentActivity().supportFragmentManager.popBackStack()
    }

    private fun setupBondViews() {
        depositEditText.setText(Constants.MIN_DEPOSIT_PRICE.toString())
        maxAuctionCountView.text = getString(R.string.max_auction_shares_count, (depositCount / product!!.basicUnitPrice).toInt())
        if (product != null){
            subButton.setOnClickListener {
                if (depositCount - Constants.ADD_INTERVAL >= Constants.MIN_DEPOSIT_PRICE){
                    depositCount -= Constants.ADD_INTERVAL
                    depositEditText.setText(depositCount.toString())
                }

            }

            addButton.setOnClickListener {
                depositCount += Constants.ADD_INTERVAL
                depositEditText.setText(depositCount.toString())
            }
        }

        depositEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                depositCount = editable?.toString()!!.toInt()
                if (depositCount < Constants.MIN_DEPOSIT_PRICE){
                    depositCount = Constants.MIN_DEPOSIT_PRICE
                    depositEditText.setText(Constants.MIN_DEPOSIT_PRICE.toString())
                }
                maxAuctionCountView.text = getString(R.string.max_auction_shares_count, (depositCount / product!!.basicUnitPrice).toInt())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        payButton.setOnClickListener {
            val money = depositEditText.text.toString().toDouble()
            payDepositPresenter.payDeposit(CApplication.INSTANCE.loginUser?.token!!, product?.id!!, money)
        }
    }

    override fun payResult(alipayResult: AlipayResult) {
        alipayUtils.pay(alipayResult.alipay_url).subscribe ({ result ->
            Logger.d("alipay success: $result")
            payDepositPresenter.bondPayed(CApplication.INSTANCE.loginUser?.token!!, product?.id!!, alipayResult.paykey, result)
        }, {e -> toast(R.string.alipay_fail_toast)})
    }
    override fun notifyBackendResult(result: String) {
        RxBus.instance.post(Action(Action.PAY_BOND_SUCCESS_ACTION, ""))
        toast(result)
        toolbarBackButtonClick()
    }
    override fun notIntegrity(type: Int) {
        AppUtils.interityUserInfo(getParentActivity(), type)
    }

}// Required empty public constructor
