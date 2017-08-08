package com.zhengdianfang.dazhongbao.models.basic

/**
 * Created by dfgzheng on 07/08/2017.
 */
data class Response<T>(var msg: String, var errCode: String, var data: T)