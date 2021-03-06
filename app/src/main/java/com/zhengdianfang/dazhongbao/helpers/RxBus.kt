package com.zhengdianfang.dazhongbao.helpers

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

/**
 * Created by dfgzheng on 24/08/2017.
 */
class RxBus {
    companion object {
        var instance = RxBus()
    }

    private val mCompositeDisposable : CompositeDisposable =  CompositeDisposable()

    private val publishSubject = PublishSubject.create<Action>()!!

    fun post(action: Action){
        publishSubject.onNext(action)
    }

    fun register(actionTypes: Array<String>, consumer: Consumer<Action>): Disposable? {
        val disposable = publishSubject.filter { (type) -> actionTypes.contains(type) }
                .subscribe(consumer)
        mCompositeDisposable.add(disposable)
        return disposable
    }
    fun register(actionType: String, consumer: Consumer<Action>): Disposable? {
        val disposable = publishSubject.filter { (type) -> actionType == type }
                .subscribe(consumer)
        mCompositeDisposable.add(disposable)
        return disposable
    }

    fun unregister(disposable: Disposable?){
        mCompositeDisposable.remove(disposable)
    }
    fun unregister(disposables: Array<Disposable?>){
        disposables.forEach {
            mCompositeDisposable.remove(it)
        }
    }
}

data class Action(var type: String, var data: Any){
    companion object {
        val FOLLOW_PRODUCT_ACTION = "follow_product_action"
        val CANCEL_FOLLOW_PRODUCT_ACTION = "cancel_follow_product_action"
        val REMOVE_BID_ACTION = "remove_bid_action"
        val ADD_BID_ACTION = "add_bid_action"
        val WEIXIN_OUTH_RESULT_ACTION = "weixin_outh_result_action"
        val WEIXIN_OUTH_RESULT_FAIL_ACTION = "WEIXIN_OUTH_RESULT_FAIL_ACTION"
        val HOME_PAGE_REFRESH_START_ACTION = "home_page_refresh_start_action"
        val HOME_PAGE_REFRESH_END_ACTION = "home_page_refresh_end_action"
        val PAY_BOND_SUCCESS_ACTION = "pay_bond_success_action"
        val LOGOUT_ACTION = "logout_action"
    }
}

data class RemoveBidResult(var productId: Long, var bidId: Long)