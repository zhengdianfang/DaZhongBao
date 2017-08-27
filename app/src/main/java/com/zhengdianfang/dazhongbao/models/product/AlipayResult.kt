package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 27/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class AlipayResult(var alipay_url: String, var paykey: String, var msg: String)