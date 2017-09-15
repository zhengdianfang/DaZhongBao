package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 20/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Deal(var realname: String, var price: Double, var count: Int )