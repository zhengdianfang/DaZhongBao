package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 08/08/2017.
 */
interface IUploadCard : IView{
    fun uploadSuccess(user: User)
}