package com.zhengdianfang.dazhongbao.presenters.validates

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 13/08/2017.
 */
class PushProductValidate(mIView: IView?) : BaseValidate(mIView) {


    fun validateFields(sharesCodes: String, companyName: String,  basicUnitPrice: Double,
                    soldCount: Int): Boolean {
       var res = true
        if (sharesCodes.isNullOrEmpty()){
            res = false
            mIView?.validateErrorUI(R.string.please_input_shares_code)
        }else if (companyName.isNullOrEmpty()){
            res = false
            mIView?.validateErrorUI(R.string.please_input_company_owner_name)
        }else if (basicUnitPrice == 0.0){
            res = false
            mIView?.validateErrorUI(R.string.please_input_basic_unit_price)
        }else if (soldCount == 0){
            res = false
            mIView?.validateErrorUI(R.string.please_input_sold_count)
        }
        return res
    }


}