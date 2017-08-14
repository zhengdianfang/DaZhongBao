package com.zhengdianfang.dazhongbao.presenters.validates

import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 13/08/2017.
 */
abstract class BaseValidate(val mIView: IView?) {

    fun checkLogin(): Boolean {
        if(CApplication.INSTANCE.isLogin()){
            return true
        }
        mIView?.noLogin()
        return false
    }

}