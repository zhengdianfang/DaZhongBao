package com.zhengdianfang.dazhongbao.models.basic

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.MessageApi
import io.reactivex.Observable

/**
 * Created by dfgzheng on 03/09/2017.
 */
class NotifyMessageRepository {

    fun fetchNotifyMessageCount(token: String): Observable<MutableList<MessageCount>> {
        return API.appClient.create(MessageApi::class.java).fetchNotifyMessageCount(token)
                .map {data ->
                    val messages = mutableListOf<MessageCount>()
                    data.get("data").forEachIndexed { index, jsonNode  ->
                        if (jsonNode.isObject) {
                            if (index == 1 || index == 2) {
                                val message = API.objectMapper.readValue(jsonNode.toString(), MessageCount::class.java)
                                message.iconType = index
                                messages.add(message)
                            }
                        }
                    }
                    val imUser = API.objectMapper.readValue(data.get("csm_user").toString(), IMUser::class.java)
                    IMUtils.sendTxtMessage(imUser, "欢迎来到大宗宝")
                    messages
                }
    }


    fun fetchMessagerListByType(token: String, icon_type: Int): Observable<MutableList<Message>>{
        return API.appClient.create(MessageApi::class.java).fetchMessageListByType(token, icon_type)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Message>>(data, object : TypeReference<MutableList<Message>>(){})}

    }
}