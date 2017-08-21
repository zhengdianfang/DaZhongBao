package com.zhengdianfang.dazhongbao.views.components

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.presenters.FollowProductPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity

/**
 * Created by dfgzheng on 05/08/2017.
 */
class RecyclerViewAdapter(private  val context: Context, private val products: MutableList<Product>) : RecyclerView.Adapter<ProductItemViewHolder>(), FollowProductPresenter.IFollowProductView  {
    private val  followProductPresenter = FollowProductPresenter()

    init {
        followProductPresenter.attachView(this)
    }

    fun destory() {
       followProductPresenter.detachView()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ProductItemViewHolder {
        return ProductItemViewHolder(
                LayoutInflater.from(parent?.context).inflate(R.layout.fragment_home_product_item_layout, parent, false),
                        followProductPresenter)
    }

    override fun getItemCount(): Int {
       return products.size
    }

    override fun showLoadingDialog() {
        (context as BaseActivity).showLoadingDialog()
    }

    override fun hideLoadingDialog() {
        (context as BaseActivity).hideLoadingDialog()
    }

    override fun validateErrorUI(errorMsgResId: Int) {
        (context as BaseActivity).validateErrorUI(errorMsgResId)
    }

    override fun networkError(errorMsg: String) {
        (context as BaseActivity).networkError(errorMsg)
    }

    override fun noLogin() {
        (context as BaseActivity).noLogin()
    }

    override fun followSuccess(msg: String, productId: Long) {
        (context as BaseActivity).toast(msg)
        val filters = products.filter { it.id == productId }
        filters.forEach {
            it.attention = 1
            val pos = products.indexOf(it)
            notifyItemChanged(pos)
        }
    }

    override fun onBindViewHolder(holder: ProductItemViewHolder?, position: Int) {
        holder?.setData(products[position])
    }
}