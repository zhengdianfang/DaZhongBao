package com.zhengdianfang.dazhongbao.views.setting


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.user.PhoneNumberVerifyFragment
import com.zhengdianfang.dazhongbao.views.user.VerfiyPasswordFragment


/**
 * A simple [Fragment] subclass.
 */
class AccountSettingFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_account_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<ViewGroup>(R.id.passwordViewGroup)!!.setOnClickListener {
            val fragment = PhoneNumberVerifyFragment()
            val bundle = Bundle()
            bundle.putString("from", "setting")
            fragment.arguments = bundle
            startFragment(android.R.id.content, fragment ,"setting")
        }

        view?.findViewById<ViewGroup>(R.id.phoneNumberViewGroup)!!.setOnClickListener {

            startFragment(android.R.id.content, VerfiyPasswordFragment(),"setting")
        }
    }

}// Required empty public constructor
