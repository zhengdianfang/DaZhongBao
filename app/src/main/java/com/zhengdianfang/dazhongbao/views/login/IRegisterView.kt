package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User

/**
 * Created by dfgzheng on 02/08/2017.
 */
interface IRegisterView:  IVerifySmsCode{
    fun receiverUser(user: User)
}