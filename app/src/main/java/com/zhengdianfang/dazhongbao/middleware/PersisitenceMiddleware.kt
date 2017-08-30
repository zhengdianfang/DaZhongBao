package com.zhengdianfang.dazhongbao.middleware

import android.content.SharedPreferences
import com.miracle.redux.Dispatcher
import com.miracle.redux.Middleware
import com.miracle.redux.Store
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.reducers.AppState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dfgzheng on 30/08/2017.
 */
class PersistenceMiddleware(private val sharedPreferences: SharedPreferences): Middleware<AppState> {

    override fun create(store: Store<AppState>?, nextDispatcher: Dispatcher?): Dispatcher {
        return Dispatcher {action ->
            nextDispatcher?.dispatch(action)
            store?.state?.version?.plus(1)
            Logger.d("save states cache ${store?.state}")
            sharedPreferences.edit().putString("last_store_states", API.objectMapper.writeValueAsString(store?.state)).apply()
        }
    }

    fun getStore(): Observable<AppState> {
        return Observable.just(getLastestStore())
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())

    }

    private fun getLastestStore(): AppState {
        var state = AppState(myAnttentionProductList = mutableListOf(), myProductList = mutableListOf(), myAuctionProductList = mutableListOf())
        val json = sharedPreferences.getString("last_store_states", "")
        if (json.isNotEmpty()){
            state = API.objectMapper.readValue(json, AppState::class.java)
        }
        Logger.d("get states cache $state")
        return state
    }
}

