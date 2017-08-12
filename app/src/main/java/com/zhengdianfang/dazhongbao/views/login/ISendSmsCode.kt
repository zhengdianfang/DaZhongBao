package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 12/08/2017.
 */
interface ISendSmsCode: IView {
    fun receiverSmsCode(code: String)
}