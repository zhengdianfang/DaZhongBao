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

    fun register(actionType: String, consumer: Consumer<Any>): Disposable? {
        val disposable = publishSubject.filter { (type) -> type == actionType }
                .map { action -> action.data }
                .subscribe(consumer)
        mCompositeDisposable.add(disposable)
        return disposable
    }

    fun unregister(disposable: Disposable?){
        mCompositeDisposable.remove(disposable)
    }
}

data class Action(var type: String, var data: Any){
    companion object {
        val FOLLOW_PRODUCT_ACTION = "follow_product_action"
    }
}