package com.zhengdianfang.dazhongbao.helpers

import android.app.Activity
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.zhengdianfang.dazhongbao.models.api.CException
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
                    if (!TextUtils.isEmpty(result["result"])){
                        observer.onNext(result)
                    }else {
                        observer.onError(CException(result["memo"] ?: "", result["resultStatus"]?.toIntOrNull()!!))
                    }
                }catch (e : Exception){
                    observer.onError(e)
                }
                observer.onComplete()
            }
        }.subscribeOn(Schedulers.newThread()).subscribeOn(AndroidSchedulers.mainThread())
    }
}