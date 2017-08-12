package com.zhengdianfang.dazhongbao.views.user

import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 12/08/2017.
 */
interface IModifyPhoneNumberView : IView{
    fun modifySuccess(user: User)
}