package com.zhengdianfang.dazhongbao.presenters

import com.hyphenate.chat.EMConversation
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.models.basic.GMessageCount
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
                IMUtils.getConversationList(), BiFunction<GMessageCount, MutableList<EMConversation>,
                           Pair<GMessageCount, MutableList<EMConversation>>> { messageCounts, list-> Pair(messageCounts, list)})
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
       addSubscription(observable, Consumer {(first, second) ->
           (mView as INofiyMessageCountAndConversatonsList).receiverList(first, second)
       })
    }

    interface INofiyMessageCountAndConversatonsList: IView{
        fun receiverList(gMessageCount: GMessageCount, conversations: MutableList<EMConversation>)
    }
}