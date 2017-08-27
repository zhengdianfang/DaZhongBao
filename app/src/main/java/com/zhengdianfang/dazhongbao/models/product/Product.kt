package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 05/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Product(var id: Long, var sharesCode: String, var sharesName: String, var yestodayClosePrice: Double,
                   var companyName: String?, var lastUnitPrice: Double, var nowUnitPrice: Double, var basicUnitPrice: Double,
                   var soldCount: Long, var limitTime: Int, var description: String?, var bidCount: Int, var check_status: Int,
                    var industry: String?, var attention: Int, var bond: Double, var bond_status: Int, var deal: MutableList<Deal>,
                   var startDateTime: Long, var endDateTime: Long, var mybids: MutableList<Bid>?) {

    override fun equals(other: Any?): Boolean {
        return id == (other as Product).id
    }
}
