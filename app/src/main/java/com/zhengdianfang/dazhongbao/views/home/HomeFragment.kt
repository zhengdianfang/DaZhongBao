package com.zhengdianfang.dazhongbao.views.home


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Advert
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.BaseFragment
import com.zhengdianfang.dazhongbao.views.home.adapter.HomeRecyclerViewAdapter

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment<MainActivity>() {

    private val products = mutableListOf<Product>()
    private val adverts = mutableListOf<Advert>()
    private val homeRecyclerView by lazy { view?.findViewById<RecyclerView>(R.id.homeRecyclerView)!! }
    private val homeRecyclerViewAdapter by lazy { HomeRecyclerViewAdapter(products, adverts) }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeRecyclerView.adapter = homeRecyclerViewAdapter

    }
}// Required empty public constructor
