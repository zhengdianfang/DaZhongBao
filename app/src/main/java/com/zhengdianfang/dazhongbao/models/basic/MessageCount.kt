package com.zhengdianfang.dazhongbao.models.basic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 03/09/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class MessageCount(var count: Int, var name: String, var message: String, var gcount: Int, var lastTime: Long)

@JsonIgnoreProperties(ignoreUnknown = true)
data class GMessageCount(var MessageCount: Int, var Message1: MessageCount, var Message2: MessageCount, var Message3: MessageCount, var Message4: MessageCount)
