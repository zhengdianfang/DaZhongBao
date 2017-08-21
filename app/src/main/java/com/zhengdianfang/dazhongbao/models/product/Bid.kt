package com.zhengdianfang.dazhongbao.models.product

/**
 * Created by dfgzheng on 20/08/2017.
 */
data class Bid(var realname: String, var price: Double, var count: Int, var productId: Long ,
               var ctime: Long, var sharesName: String, var sharesCode: String)