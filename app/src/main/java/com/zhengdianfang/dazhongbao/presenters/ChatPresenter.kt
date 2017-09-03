package com.zhengdianfang.dazhongbao.presenters

import com.hyphenate.chat.EMMessage
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.login.UserRepository
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by dfgzheng on 03/09/2017.
 */
class ChatPresenter: BasePresenter() {
    private val userRepository = UserRepository()

    fun fetchChatList(userId: String){
        mView?.showLoadingDialog()
        val loginUser = CApplication.INSTANCE.loginUser
        val observable = Observable.zip(userRepository.fetchIMUserInfo(loginUser?.token!!, userId).map { list -> list.first() },
                IMUtils.getMessageList(userId), BiFunction<User, MutableList<EMMessage>, Pair<User, MutableList<EMMessage>>> { user, list-> Pair(user, list)})
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        addSubscription(observable, Consumer { (first, second) ->
            (mView as IMUserInfoAndMessages).receiverUserInfoAndMessages(first, second)
            mView?.hideLoadingDialog()
        })
    }

    interface IMUserInfoAndMessages: IView{
        fun receiverUserInfoAndMessages(user: User, allMessage: MutableList<EMMessage>)
    }
}