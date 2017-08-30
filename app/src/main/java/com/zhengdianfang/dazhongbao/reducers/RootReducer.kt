package com.zhengdianfang.dazhongbao.reducers

import com.miracle.redux.Action
import com.miracle.redux.Reducer


/**
 * Created by dfgzheng on 28/08/2017.
 */
class RootReducer(private val reducers: Array<Reducer<AppState>>): Reducer<AppState> {
    override fun reduce(state: AppState?, action: Action?): AppState? {
        var state = state
        reducers.forEach {
            state = it.reduce(state, action)
        }
        return state
    }
}