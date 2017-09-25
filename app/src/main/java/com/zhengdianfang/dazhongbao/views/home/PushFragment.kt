package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.afollestad.materialdialogs.GravityEnum
import com.afollestad.materialdialogs.MaterialDialog
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.models.product.SharesInfo
import com.zhengdianfang.dazhongbao.presenters.BasePresenter
import com.zhengdianfang.dazhongbao.presenters.PushProductPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.login.SetUserCertificationActivity


/**
 * A simple [Fragment] subclass.
 */
class PushFragment : BaseFragment(), PushProductPresenter.IPushProduct, BasePresenter.ICheckUserIntegrityView{

    private val sharesCodeEditView by lazy { view?.findViewById<AutoCompleteTextView>(R.id.sharesCodeEditView)!! }
    private val companyUnitPriceEditView by lazy { view?.findViewById<EditText>(R.id.companyUnitPriceEditView)!! }
    private val saleCountEditView by lazy { view?.findViewById<EditText>(R.id.saleCountEditView)!! }
    private val yesRadio by lazy { view?.findViewById<RadioButton>(R.id.radioYes)!! }
    private val noRadio by lazy { view?.findViewById<RadioButton>(R.id.radioNo)!! }
    private val shareOwnerNameEidtView by lazy { view?.findViewById<EditText>(R.id.shareOwnerNameEidtView)!! }
    private val detailEditView by lazy { view?.findViewById<EditText>(R.id.detailEditView)!! }
    private val pushButton by lazy { view?.findViewById<Button>(R.id.pushButton)!! }
    private val mPushProductPresenter by lazy { PushProductPresenter() }
    private val pushSuccessDialog by lazy {
        MaterialDialog.Builder(context)
                .content(R.string.push_success_content)
                .cancelable(false)
                .build()
    }
    private var selectedShareCode = ""



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_push, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPushProductPresenter.attachView(this)

        yesRadio.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                noRadio.isChecked = false
            }
        }
        noRadio.setOnCheckedChangeListener { compoundButton, b ->
            if(b) {
                yesRadio.isChecked = false
            }
        }

        pushButton.setOnClickListener {
            val companyCode = if(this.selectedShareCode.isEmpty())  sharesCodeEditView.text.toString() else this.selectedShareCode
            val companyUnitPrice = if(TextUtils.isEmpty(companyUnitPriceEditView.text.toString())) 0.0 else companyUnitPriceEditView.text.toString().toDouble()
            val soldCount = if(TextUtils.isEmpty(saleCountEditView.text.toString())) 0 else saleCountEditView.text.toString().toLong()
            val notes = detailEditView.text.toString()
            val sharesOwnerName = shareOwnerNameEidtView.text.toString()
            val limitTime = if(yesRadio.isChecked) 6L else 0
            val token = CApplication.INSTANCE.loginUser?.token ?: ""
            if (mPushProductPresenter.pushProductBeforeValidate(companyCode, sharesOwnerName, companyUnitPrice, soldCount)){
               MaterialDialog.Builder(context)
                       .content(getString(R.string.push_confirm_dialog_content, sharesOwnerName, limitTime))
                       .title(R.string.authorization_agreement)
                       .buttonsGravity(GravityEnum.CENTER)
                       .positiveColor(ContextCompat.getColor(context, R.color.colorPrimary))
                       .positiveText(R.string.agreeAndSubmit)
                       .onPositive { dialog, _ ->
                           dialog.dismiss()
                           mPushProductPresenter.pushProduct(token, companyCode, sharesOwnerName, companyUnitPrice, soldCount, limitTime, notes)
                       }
                       .show()
            }
        }
        sharesCodeEditView.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                val code = editable.toString()
                if(code.length >= 6){
                    val results = CApplication.INSTANCE.shareInfosCache.filter { it.code == code }
                    if (results.isNotEmpty()){
                        showDropDown(results.first())
                    }else{
                        mPushProductPresenter.getSharesInfo(CApplication.INSTANCE.loginUser?.token ?: "", code)
                    }
                }

            }

            override fun beforeTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(editable: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        })
        sharesCodeEditView.setOnItemClickListener { adapterView, view, i, l ->
           this.selectedShareCode = (adapterView.getItemAtPosition(i) as SharesInfo).code
        }
    }

    override fun onBackPressed(): Boolean {
        getParentActivity().finish()
        return true
    }

    private fun showDropDown(sharesInfo: SharesInfo) {
        val adapter =  ArrayAdapter<SharesInfo>(context, android.R.layout.simple_list_item_1, CApplication.INSTANCE.shareInfosCache.filter { it == sharesInfo })
        sharesCodeEditView.setAdapter(adapter)
        sharesCodeEditView.showDropDown()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mPushProductPresenter.detachView()
    }

    override fun receiverProduct(product: Product) {
        pushSuccessDialog.show()
        view?.postDelayed({
            pushSuccessDialog.dismiss()
            (getParentActivity() as MainActivity).resetCurrentTab()
        }, 3000)
    }

    override fun receiverSharesInfo(sharesInfo: SharesInfo) {
        CApplication.INSTANCE.shareInfosCache.add(sharesInfo)
        showDropDown(sharesInfo)
    }
    override fun notIntegrity(type: Int) {
        SetUserCertificationActivity.startActivity(getParentActivity(), type)
    }

}// Required empty public constructor
