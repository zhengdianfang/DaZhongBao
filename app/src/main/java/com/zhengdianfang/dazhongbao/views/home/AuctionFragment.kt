package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment


/**
 * A simple [Fragment] subclass.
 */
class AuctionFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_auction, container, false)
    }

}// Required empty public constructor
