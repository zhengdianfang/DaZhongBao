package com.zhengdianfang.dazhongbao.models.product

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.basic.IMUser

/**
 * Created by dfgzheng on 05/08/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class Product(var id: Long, var sharesCode: String, var sharesName: String, var yestodayClosePrice: Double,
                   var companyName: String?, var lastUnitPrice: Double, var nowUnitPrice: Double, var basicUnitPrice: Double,
                   var soldCount: Long, var limitTime: Int, var description: String?, var bidcount: Int, var check_status: Int,
                   var industry: String?, var attention: Int, var bond: String?, var bond_status: Int, var deal: MutableList<Deal>?,
                   var contact: Contact?, var startDateTime: Long, var endDateTime: Long, var mybids: MutableList<Bid>?, var csm_user: IMUser?) {

    override fun equals(other: Any?): Boolean {
        return id == (other as Product).id
    }

    fun isSeller(): Boolean {
       return contact?.id === CApplication.INSTANCE.loginUser?.id
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Contact(var id: String, var nickname: String, var username: String)
