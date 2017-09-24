package com.zhengdianfang.dazhongbao.models.login

import android.text.TextUtils
import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by dfgzheng on 30/07/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class User(var id: String?, var token: String?, var realname: String?, var avatar: String?,
                var companyName: String?, var companyAddress: String?, var email: String? ,
                var phonenumber: String?, var position: String?,
                var level: Int, var type: Int, var integrity: Int, var businessCard: String?, var contactCard: String?,
                var contactCard2: String?, var contactCardStatus: String?,
                var businessCardStatus: String?, var businessLicence: String?,
                var businessLicenceStatus: String?) {

    companion object {
        val PERSONAL_TYPE = 1
        val ORGANIZATION_TYPE = 2
    }

    fun isExitsBusinessCard(): Boolean {
        return !TextUtils.isEmpty(businessCardStatus) && businessCardStatus != "0"
    }
    fun isExitsContactCard(): Boolean {
        return !TextUtils.isEmpty(contactCardStatus) && contactCardStatus != "0"
    }
    fun isExitsBusinessLicenceCard(): Boolean {
        return !TextUtils.isEmpty(businessLicenceStatus) && businessLicenceStatus != "0"
    }
}

