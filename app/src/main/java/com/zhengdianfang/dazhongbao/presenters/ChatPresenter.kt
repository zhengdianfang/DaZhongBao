package com.zhengdianfang.dazhongbao.presenters

import android.text.TextUtils
import com.hyphenate.EMMessageListener
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMMessage
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.models.basic.IMUser
import com.zhengdianfang.dazhongbao.models.login.UserRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 03/09/2017.
 */
class ChatPresenter: BasePresenter() , EMMessageListener {

    private val userRepository = UserRepository()

    override fun attachView(v: IView?) {
        super.attachView(v)
        EMClient.getInstance().chatManager().addMessageListener(this)
    }

    override fun detachView() {
        super.detachView()
        EMClient.getInstance().chatManager().removeMessageListener(this)
    }
    fun fetchChatList(userId: String){
        addSubscription(IMUtils.getMessageList(userId), Consumer {list ->
            (mView as IMUserInfoAndMessages).receiverMessages(list)
        })
    }

    fun sendTextMessage(user: IMUser, msg: String) {
        if (TextUtils.isEmpty(msg)){
            mView?.validateErrorUI(R.string.please_input_txt)
            return
        }
        IMUtils.sendTxtMessage(user, msg)
        addSubscription(IMUtils.getMessageList(user.id!!).delay(100, TimeUnit.MILLISECONDS), Consumer {messages ->
            (mView as IMUserInfoAndMessages).updateMessages(messages ?: mutableListOf())
        })
    }

    fun sendVoiceMessage(user: IMUser, filePath: String, length: Int) {
        if (TextUtils.isEmpty(filePath) || !File(filePath).exists()){
            mView?.validateErrorUI(R.string.please_input_legal_voice)
            return
        }
        IMUtils.sendVoiceMessage(filePath, length, user)
        addSubscription(IMUtils.getMessageList(user.id!!).delay(100, TimeUnit.MILLISECONDS), Consumer { messages ->
            (mView as IMUserInfoAndMessages).updateMessages(messages ?: mutableListOf())
        })
    }

    override fun onMessageChanged(message: EMMessage?, change: Any?) {
        Logger.d("onMessageChanged")

    }

    override fun onCmdMessageReceived(messages: MutableList<EMMessage>?) {
        Logger.d("onCmdMessageReceived")
    }

    override fun onMessageReceived(messages: MutableList<EMMessage>?) {
        Logger.d("onMessageReceived , receiver message size : ${messages?.count()}")
        Observable.just(messages).observeOn(AndroidSchedulers.mainThread())
                .subscribe {messages ->
                    (mView as IMUserInfoAndMessages).addMessages(messages ?: mutableListOf())
                }
    }

    override fun onMessageDelivered(messages: MutableList<EMMessage>?) {
        Logger.d("onMessageDelivered")
    }

    override fun onMessageRead(messages: MutableList<EMMessage>?) {
        Logger.d("onMessageRead")
    }

    interface IMUserInfoAndMessages: IView{
        fun receiverMessages(allMessage: MutableList<EMMessage>)
        fun updateMessages(messages: MutableList<EMMessage>)
        fun addMessages(messages: MutableList<EMMessage>)
    }
}