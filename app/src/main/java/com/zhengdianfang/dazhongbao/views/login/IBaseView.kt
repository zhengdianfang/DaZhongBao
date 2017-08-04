package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 02/08/2017.
 */
interface IBaseView: IView {
    fun validateErrorUI(errorMsgResId: Int)
}