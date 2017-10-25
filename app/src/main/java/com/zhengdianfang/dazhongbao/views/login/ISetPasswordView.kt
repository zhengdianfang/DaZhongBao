package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 09/08/2017.
 */
interface ISetPasswordView: IView{
    fun setPasswordSuccess(user: User)
}