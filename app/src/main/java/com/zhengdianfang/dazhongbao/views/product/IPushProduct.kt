package com.zhengdianfang.dazhongbao.views.product

import com.zhengdianfang.dazhongbao.models.product.Product
import com.zhengdianfang.dazhongbao.views.basic.IView

/**
 * Created by dfgzheng on 13/08/2017.
 */
interface IPushProduct: IView {
    fun receiverProduct(product: Product)
}