package com.zhengdianfang.dazhongbao.models.basic

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 17/09/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Message(var type: String, var icon_type: String, var op_type: String , var name: String, var desc: String,
                   var banner:String? , var url: String?, var cid: String?, var ctime: Long)