package com.zhengdianfang.dazhongbao.views.basic

/**
 * Created by dfgzheng on 25/07/2017.
 */
interface IView<T> {
    fun showLoadingDailog()
    fun hideLoadingDailog()
    fun renderResponse(data: T)
}