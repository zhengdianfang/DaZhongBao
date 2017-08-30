package com.zhengdianfang.dazhongbao.reducers

import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 28/08/2017.
 */
data class AppState (var myProductList : MutableList<Product>,
                     var myAnttentionProductList : MutableList<Product>,
                     var myAuctionProductList : MutableList<Product>,
                     var version: Int = 0){
    constructor() : this(mutableListOf(), mutableListOf(), mutableListOf(), 0)
}

