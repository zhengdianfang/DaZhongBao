package com.zhengdianfang.dazhongbao.views.login

/**
 * Created by dfgzheng on 08/08/2017.
 */
interface IVerifySmsCode: IBaseView {
    fun verifySmsCodeResult(success: Boolean)
    fun receiverSmsCode(code: String)
}