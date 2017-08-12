package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 08/08/2017.
 */
interface IVerifySmsCode: IView {
    fun verifySmsCodeResult(success: Boolean)
}