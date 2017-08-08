package com.zhengdianfang.dazhongbao.views.basic

/**
 * Created by dfgzheng on 25/07/2017.
 */
interface IView {
    fun showLoadingDialog()
    fun hideLoadingDialog()
    fun networkError(msg: String)
}