package com.zhengdianfang.dazhongbao.models.basic

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
                    jsonNode.forEach {
                        if (it.isObject) {
                            val message = API.objectMapper.readValue(it.toString(), MessageCount::class.java)
                            messages.add(message)
                        }
                    }
                    messages
                }
    }
}