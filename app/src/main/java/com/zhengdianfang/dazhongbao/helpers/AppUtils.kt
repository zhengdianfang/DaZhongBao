package com.zhengdianfang.dazhongbao.helpers

import android.support.v4.app.Fragment
import com.zhengdianfang.dazhongbao.presenters.validates.UserInfoInterityValidate
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.login.SetOrganizationInfoFragment
import com.zhengdianfang.dazhongbao.views.login.UploadBusinessCardFragment
import com.zhengdianfang.dazhongbao.views.login.UploadContactCardFragment

/**
 * Created by dfgzheng on 24/09/2017.
 */
object AppUtils {

    fun interityUserInfo(activity: BaseActivity, type: Int){
        when(type) {
            UserInfoInterityValidate.NO_EXITS_BUSINESS_CARD -> {
                activity.startFragment(android.R.id.content,
                        Fragment.instantiate(activity, UploadBusinessCardFragment::class.java.name),
                        "cert")
            }
            UserInfoInterityValidate.NO_EXITS_BUSINESS_LINLENCE_CARD -> {
                activity.startFragment(android.R.id.content,
                        Fragment.instantiate(activity, SetOrganizationInfoFragment::class.java.name),
                        "cert")
            }
            UserInfoInterityValidate.NO_EXITS_CONTACT_CARD -> {

                activity.startFragment(android.R.id.content,
                        Fragment.instantiate(activity, UploadContactCardFragment::class.java.name),
                        "cert")
            }
        }
    }
}