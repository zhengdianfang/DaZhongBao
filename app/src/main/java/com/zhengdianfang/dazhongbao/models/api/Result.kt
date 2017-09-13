package com.zhengdianfang.dazhongbao.models.api

/**
 * Created by dfgzheng on 13/09/2017.
 */
data class Result<T>(var data: T, var isCache: Boolean = false)