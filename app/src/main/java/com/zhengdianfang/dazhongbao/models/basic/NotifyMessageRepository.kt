package com.zhengdianfang.dazhongbao.models.basic

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.MessageApi
import io.reactivex.Observable

/**
 * Created by dfgzheng on 03/09/2017.
 */
class NotifyMessageRepository {

    fun fetchNotifyMessageCount(token: String): Observable<MutableList<MessageCount>> {
        return API.appClient.create(MessageApi::class.java).fetchNotifyMessageCount(token)
                .map {response -> API.parseResponse(response) }
                .map {data ->
                    val messages = mutableListOf<MessageCount>()
                    val jsonNode = API.objectMapper.readTree(data)
                    jsonNode.forEachIndexed { index, jsonNode  ->
                        if (jsonNode.isObject) {
                            val message = API.objectMapper.readValue(jsonNode.toString(), MessageCount::class.java)
                            message.iconType = index
                            messages.add(message)
                        }
                    }
                    messages
                }
    }


    fun fetchMessagerListByType(token: String, icon_type: Int): Observable<MutableList<Message>>{
        return API.appClient.create(MessageApi::class.java).fetchMessageListByType(token, icon_type)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Message>>(data, object : TypeReference<MutableList<Message>>(){})}

    }
}