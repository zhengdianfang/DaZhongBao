package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.zhengdianfang.dazhongbao.CApplication

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import jp.wasabeef.glide.transformations.CropCircleTransformation


/**
 * A simple [Fragment] subclass.
 */
class PersonalFragment : BaseFragment(){
    private val avatarImageView by lazy { view?.findViewById<ImageView>(R.id.userHeaderView)!! }
    private val userRealNameTextView by lazy { view?.findViewById<TextView>(R.id.userNameTextview)!! }
    private val userPhoneNumberTextView by lazy { view?.findViewById<TextView>(R.id.phoneNumberTextView)!! }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val loginUser = CApplication.INSTANCE.loginUser
        Glide.with(this).load(loginUser?.avatar).apply(RequestOptions.bitmapTransform(CropCircleTransformation(this.context))).into(avatarImageView)
        userRealNameTextView.text = loginUser?.realname
        userPhoneNumberTextView.text = loginUser?.phonenumber
    }

}// Required empty public constructor
