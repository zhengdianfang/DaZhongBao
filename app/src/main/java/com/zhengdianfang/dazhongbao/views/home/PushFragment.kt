package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.ProductPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.product.IPushProduct


/**
 * A simple [Fragment] subclass.
 */
class PushFragment : BaseFragment(), IPushProduct{

    private val companyCodeEditView by lazy { view?.findViewById<EditText>(R.id.companyCodeEditView)!! }
    private val companyUnitPriceEditView by lazy { view?.findViewById<EditText>(R.id.companyUnitPriceEditView)!! }
    private val saleCountEditView by lazy { view?.findViewById<EditText>(R.id.saleCountEditView)!! }
    private val limitRadioGroup by lazy { view?.findViewById<RadioGroup>(R.id.limitRadioGroup)!! }
    private val shareOwnerNameEidtView by lazy { view?.findViewById<EditText>(R.id.shareOwnerNameEidtView)!! }
    private val detailEditView by lazy { view?.findViewById<EditText>(R.id.detailEditView)!! }
    private val pushButton by lazy { view?.findViewById<Button>(R.id.pushButton)!! }
    private val mProductPresenter by lazy { ProductPresenter() }



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_push, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mProductPresenter.attachView(this)

        limitRadioGroup.setOnCheckedChangeListener { radioGroup, id ->
            radioGroup.clearCheck()
            view?.findViewById<RadioButton>(id)?.isChecked = true

        }

        pushButton.setOnClickListener {
            val companyCode = companyCodeEditView.text.toString()
            val companyUnitPrice = if(TextUtils.isEmpty(companyUnitPriceEditView.text.toString())) 0.0 else companyUnitPriceEditView.text.toString().toDouble()
            val soldCount = if(TextUtils.isEmpty(saleCountEditView.text.toString())) 0 else saleCountEditView.text.toString().toInt()
            val notes = detailEditView.text.toString()
            val sharesOwnerName = shareOwnerNameEidtView.text.toString()
            val limitTime = if(view?.findViewById<RadioButton>(R.id.radioYes)?.isChecked ?: false) 6L else 0
            val token = CApplication.INSTANCE.loginUser?.token ?: ""
            if (mProductPresenter.pushProductBeforeValidate(companyCode, sharesOwnerName, companyUnitPrice, soldCount)){
               MaterialDialog.Builder(context)
                       .content(getString(R.string.push_confirm_dialog_content, sharesOwnerName, limitTime))
                       .title(R.string.authorization_agreement)
                       .buttonsGravity(GravityEnum.CENTER)
                       .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                       .positiveText(R.string.agreeAndSubmit)
                       .onPositive { dialog, _ ->
                           dialog.dismiss()
                           mProductPresenter.pushProduct(token, companyCode, sharesOwnerName, companyUnitPrice, soldCount, limitTime, notes)
                       }
                       .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mProductPresenter.detachView()
    }

    override fun receiverProduct(product: Product) {
        toast(R.string.push_success)
    }

}// Required empty public constructor
