package com.zhengdianfang.dazhongbao.views.product

import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 10/08/2017.
 */
interface IProductList: IView {
   fun receiverProductList(list: List<Product>)
}