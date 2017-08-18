package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 16/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class SharesInfo(var code: String, var name: String){
    override fun toString(): String {
        return name
    }

    override fun equals(other: Any?): Boolean {
        return code == (other as SharesInfo).code
    }
}