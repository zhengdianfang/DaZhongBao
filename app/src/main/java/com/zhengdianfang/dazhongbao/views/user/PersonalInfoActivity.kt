package com.zhengdianfang.dazhongbao.views.user

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity

class PersonalInfoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_info)
    }

    override fun onResume() {
        super.onResume()
        val loginUser = CApplication.INSTANCE.loginUser
        if (null != loginUser){
            findViewById<TextView>(R.id.userNameTextView)!!.text = loginUser.realname
            Glide.with(this).load(loginUser.avatar).into(findViewById<ImageView>(R.id.userHeaderView))
            findViewById<TextView>(R.id.companyNameTextView)!!.text = loginUser.companyName
            findViewById<TextView>(R.id.companyPositonTextView)!!.text = loginUser.position
            findViewById<TextView>(R.id.companyLocationTextView)!!.text = loginUser.companyAddress
            findViewById<TextView>(R.id.emailTextView)!!.text = loginUser.email
        }
    }

    override fun onBackPressed() {
        finish()
    }
}
