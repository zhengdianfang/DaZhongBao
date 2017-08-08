package com.zhengdianfang.dazhongbao.models.api

/**
 * Created by dfgzheng on 07/08/2017.
 */
class CException(msg: String, val errCode: Int) : Throwable(msg){
}