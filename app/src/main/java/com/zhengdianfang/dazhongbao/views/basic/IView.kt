package com.zhengdianfang.dazhongbao.views.basic

/**
 * Created by dfgzheng on 25/07/2017.
 */
interface IView {
    fun showLoadingDialog()
    fun hideLoadingDialog()
    fun validateErrorUI(errorMsgResId: Int)
    fun networkError(errorMsg: String)
    fun noLogin()
}