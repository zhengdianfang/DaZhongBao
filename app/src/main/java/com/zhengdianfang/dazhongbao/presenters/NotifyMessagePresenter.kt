package com.zhengdianfang.dazhongbao.presenters

import com.hyphenate.chat.EMConversation
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.models.basic.Message
import com.zhengdianfang.dazhongbao.models.basic.MessageCount
import com.zhengdianfang.dazhongbao.models.basic.NotifyMessageRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by dfgzheng on 03/09/2017.
 */
class NotifyMessagePresenter: BasePresenter() {
    private val messageRepository = NotifyMessageRepository()

    fun fetchNotifyMessageCount(token: String){
        val observable = Observable.zip(messageRepository.fetchNotifyMessageCount(token),
                IMUtils.getConversationList(), BiFunction<MutableList<MessageCount>, MutableList<EMConversation>,
                           Pair<MutableList<MessageCount>, MutableList<EMConversation>>> { messageCounts, list-> Pair(messageCounts, list)})
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
       addSubscription(observable, Consumer {(first, second) ->
           (mView as INofiyMessageCountAndConversatonsList).receiverList(first, second)
       })
    }

    fun fetchMessagerListByType(token: String, icon_type: Int){
        addSubscription(messageRepository.fetchMessagerListByType(token, icon_type), Consumer {list ->
            (mView as IMessageList).receiverList(list)
        })
    }

    interface INofiyMessageCountAndConversatonsList: IView{
        fun receiverList(messageCounts: MutableList<MessageCount>, conversations: MutableList<EMConversation>)
    }

    interface IMessageList: IView{
        fun receiverList(messages: MutableList<Message>)
    }
}