package com.zhengdianfang.dazhongbao.views.login

import com.zhengdianfang.dazhongbao.models.login.User

/**
 * Created by dfgzheng on 08/08/2017.
 */
interface IUploadCard : IBaseView {
    fun uploadSuccess(user: User)
}