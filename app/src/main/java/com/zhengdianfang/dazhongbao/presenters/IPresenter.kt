package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 25/07/2017.
 */
interface IPresenter<T, V : IView<T>> {
    fun attachView(v: V)
    fun detachView()
}