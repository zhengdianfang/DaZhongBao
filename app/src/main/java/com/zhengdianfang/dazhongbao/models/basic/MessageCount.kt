package com.zhengdianfang.dazhongbao.models.basic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 03/09/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageCount(var count: Int, var name: String, var message: String, var gcount: Int, var lastTime: Long)
