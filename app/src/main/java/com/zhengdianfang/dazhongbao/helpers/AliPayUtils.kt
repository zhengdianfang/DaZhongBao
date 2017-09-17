package com.zhengdianfang.dazhongbao.helpers

import android.app.Activity
import com.alipay.sdk.app.PayTask
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dfgzheng on 16/09/2017.
 */
class AliPayUtils(private val activity: Activity) {

    fun pay(payInfo: String): Observable<Map<String, String>> {
        return Observable.create<Map<String, String>> {observer ->
            if (observer.isDisposed.not()) {
                val payTask = PayTask(activity)
                try {
                    val result = payTask.payV2(payInfo, true)
                    observer.onNext(result)
                }catch (e : Exception){
                    observer.onError(e)
                }
                observer.onComplete()
            }
        }.subscribeOn(Schedulers.newThread()).subscribeOn(AndroidSchedulers.mainThread())
    }
}