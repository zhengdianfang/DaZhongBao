package com.zhengdianfang.dazhongbao.models.product

import com.zhengdianfang.dazhongbao.models.login.User

/**
 * Created by dfgzheng on 05/08/2017.
 */
data class Product(var id: String, var companyCode: String, var productName: String,
                   var lastUnitPrice: Double, var nowUnitPrice: Double, var basicUnitPrice: Double,
                   var soldCount: Int, var limitTime: Long, var description: String,
                   var bidCount: Int, var status: Int, var startDateTime: Long, var contact: User,
                   var industry: String
)