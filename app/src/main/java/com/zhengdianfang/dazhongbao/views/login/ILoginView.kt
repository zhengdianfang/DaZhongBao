package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.basic.IView


/**
 * Created by dfgzheng on 30/07/2017.
 */
interface ILoginView : IView{
    fun userResponseProcessor(user: User?)
}

