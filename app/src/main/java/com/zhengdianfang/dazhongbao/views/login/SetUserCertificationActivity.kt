package com.zhengdianfang.dazhongbao.views.login

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.validates.UserInfoInterityValidate
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity

/**
 * Created by dfgzheng on 24/09/2017.
 */
class SetUserCertificationActivity: BaseActivity() {
    companion object {
        fun startActivity(activity: BaseActivity, type: Int) {
            activity.startActivity(Intent(activity, SetUserCertificationActivity::class.java).putExtra("type", type))
            activity.overridePendingTransition(0, 0)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.getBooleanExtra("register", false)){
            val loginUser = CApplication.INSTANCE.loginUser
            if (loginUser?.type == 1){
                startFragment(android.R.id.content, Fragment.instantiate(this, UploadBusinessCardFragment::class.java.name))
            }else{
                startFragment(android.R.id.content, Fragment.instantiate(this, RegisterSetOrganizationInfoFragment::class.java.name))
            }
        }else{
            when(intent.getIntExtra("type", -1)) {
                UserInfoInterityValidate.NO_EXITS_BUSINESS_CARD -> {
                    startFragment(android.R.id.content,
                            Fragment.instantiate(this, UploadBusinessCardFragment::class.java.name),
                            "cert")
                }
                UserInfoInterityValidate.NO_EXITS_BUSINESS_LINLENCE_CARD -> {
                    startFragment(android.R.id.content,
                            Fragment.instantiate(this, SetOrganizationInfoFragment::class.java.name),
                            "cert")
                }
                UserInfoInterityValidate.NO_EXITS_CONTACT_CARD -> {

                    startFragment(android.R.id.content,
                            Fragment.instantiate(this, UploadContactCardFragment::class.java.name),
                            "cert")
                }
            }
        }
    }
    class RegisterSetOrganizationInfoFragment: SetOrganizationInfoFragment() {
        override fun uploadSuccess(user: User) {
            CApplication.INSTANCE.loginUser = user
            toast(R.string.upload_business_card_success)
            replaceFragment(android.R.id.content, Fragment.instantiate(context, UploadBusinessCardFragment::class.java.name))
        }
    }
}