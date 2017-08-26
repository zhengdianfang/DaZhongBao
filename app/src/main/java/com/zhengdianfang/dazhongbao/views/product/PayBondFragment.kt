package com.zhengdianfang.dazhongbao.views.product


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class PayBondFragment : BaseFragment() {

    private val subTextView by lazy { view?.findViewById<View>(R.id.subTextView)!! }
    private val addTextView by lazy { view?.findViewById<View>(R.id.addTextView)!! }
    private val bondEditText by lazy { view?.findViewById<EditText>(R.id.bondEditText)!! }
    private val maxAuctionCountView by lazy { view?.findViewById<TextView>(R.id.maxAuctionCountView)!! }
    var product: Product? = null
    private var bondCount = Constants.MIN_BOND_PRICE

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_pay_bond, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupBondViews()
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().supportFragmentManager.popBackStack()
        return true
    }

    private fun setupBondViews() {
        bondEditText.setText(Constants.MIN_BOND_PRICE.toString())
        maxAuctionCountView.text = getString(R.string.max_auction_shares_count, (bondCount  / product!!.basicUnitPrice).toInt())
        if (product != null){
            subTextView.setOnClickListener {
                if (bondCount - Constants.ADD_INTERVAL >= Constants.MIN_BOND_PRICE ){
                    bondCount -= Constants.ADD_INTERVAL
                    bondEditText.setText(bondCount.toString())
                }

            }

            addTextView.setOnClickListener {
                bondCount += Constants.ADD_INTERVAL
                bondEditText.setText(bondCount.toString())
            }
        }

        bondEditText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                bondCount = editable?.toString()!!.toInt()
                if (bondCount < Constants.MIN_BOND_PRICE){
                    bondCount = Constants.MIN_BOND_PRICE
                    bondEditText.setText(Constants.MIN_BOND_PRICE.toString())
                }
                maxAuctionCountView.text = getString(R.string.max_auction_shares_count, (bondCount  / product!!.basicUnitPrice).toInt())

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

}// Required empty public constructor
