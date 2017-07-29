package com.zhengdianfang.dazhongbao.presenters

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.zhengdianfang.dazhongbao.models.api.TestApi
import com.zhengdianfang.dazhongbao.views.basic.IView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
 * Created by dfgzheng on 25/07/2017.
 */
open class BasePresenter<T, V : IView<T>>: IPresenter<T, V> {
    protected var mView: IView<T>? = null
    private val mCompositeDisposable : CompositeDisposable by lazy { CompositeDisposable() }
    protected val appClient  = Retrofit.Builder()
            .baseUrl(TestApi.HOST)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

    override fun attachView(v: V) {
        this.mView = v
    }

    override fun detachView() {
        this.mView = null
    }

    fun addSubscription(observable: Observable<T>, consumer: Consumer<T>){
        mCompositeDisposable.add(observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer))


    }

    fun unsubcribe() {
        mCompositeDisposable.clear()
    }
}