package com.zhengdianfang.dazhongbao.views.basic

import android.content.Context

/**
 * Created by dfgzheng on 25/07/2017.
 */
interface IView {
    fun showLoadingDialog()
    fun hideLoadingDialog()
    fun getContext(): Context
}