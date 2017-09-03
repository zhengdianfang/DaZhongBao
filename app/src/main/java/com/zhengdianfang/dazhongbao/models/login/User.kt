package com.zhengdianfang.dazhongbao.models.login

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
                var businessLicenceStatus: String?, var im_id: String?)

