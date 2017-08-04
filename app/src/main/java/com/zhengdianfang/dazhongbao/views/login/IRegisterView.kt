package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User

/**
 * Created by dfgzheng on 02/08/2017.
 */
interface IRegisterView: IBaseView{
    fun receiverSmsCode(code: String)
    fun receiverUser(user: User)
}