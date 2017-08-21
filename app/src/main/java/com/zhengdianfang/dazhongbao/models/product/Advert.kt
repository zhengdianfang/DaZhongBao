package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 05/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Advert(var id: Long, var name: String, var banner: String, var mod: Int,
                  var productId: Long, var sharesCode: String,
                  var sharesName: String, var link: String)