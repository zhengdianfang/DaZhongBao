package com.zhengdianfang.dazhongbao.presenters

import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

/**
 * Created by dfgzheng on 25/07/2017.
 */
open class BasePresenter: IPresenter {
    protected var mView: IView? = null
    private val mCompositeDisposable : CompositeDisposable by lazy { CompositeDisposable() }
    var MOCK = false


    override fun attachView(v: IView?) {
        this.mView = v
    }

    override fun detachView() {
        this.mView = null
        unsubcribe()
    }

    fun <T : Any?> addSubscription(observable: Observable<T>, consumer: Consumer<T>){
        mCompositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, Consumer{ error ->
                         mView?.hideLoadingDialog()
                         mView?.networkError(error.message ?: "")
            }))
    }
    fun <T : Any?> addSubscription(observable: Observable<T>, consumer: Consumer<T>, errorConsumer: Consumer<in Throwable>){
        mCompositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer, errorConsumer))
    }



    fun unsubcribe() {
        mCompositeDisposable.clear()
    }

}