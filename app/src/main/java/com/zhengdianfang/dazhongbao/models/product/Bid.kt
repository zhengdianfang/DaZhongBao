package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 20/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Bid(var bidId: Long, var realname: String?, var price: Double, var count: Long, var productId: Long,
               var ctime: Long, var sharesName: String?, var sharesCode: String?, var highest: Double, var status: Int)
//status 0是撤消，前端看不到的，1是已出价，2是失败，3是成功