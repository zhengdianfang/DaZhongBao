package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User


/**
 * Created by dfgzheng on 30/07/2017.
 */
interface ILoginView : IBaseView{
    fun userResponseProcessor(user: User?)
}

