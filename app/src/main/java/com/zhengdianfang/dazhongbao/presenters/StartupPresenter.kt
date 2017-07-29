package com.zhengdianfang.dazhongbao.presenters

import android.util.Log
import com.zhengdianfang.dazhongbao.models.api.TestApi
import com.zhengdianfang.dazhongbao.views.StartupView
import io.reactivex.functions.Consumer
import retrofit2.Retrofit

/**
 * Created by dfgzheng on 25/07/2017.
 */
class StartupPresenter: BasePresenter<String, StartupView>() {
    fun loadData() {
        mView?.showLoadingDailog()
        addSubscription(appClient.create(TestApi::class.java).testApi(), object : Consumer<String> {
            override fun accept(t: String?) {
                Log.d("StartupPresenter", t)
                mView?.hideLoadingDailog()
            }
        } )
    }
}