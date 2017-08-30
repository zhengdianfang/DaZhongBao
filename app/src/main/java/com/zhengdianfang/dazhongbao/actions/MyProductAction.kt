package com.zhengdianfang.dazhongbao.actions

import com.miracle.redux.Action
import com.miracle.redux.Store
import com.zhengdianfang.dazhongbao.models.login.UserRepository
import com.zhengdianfang.dazhongbao.reducers.AppState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by dfgzheng on 28/08/2017.
 */
class MyProductAction(private val store: Store<AppState>) {
    companion object {
        val MY_PRODUCT_LIST_ACTION = "my_product_list_action"
        val MY_ATTENTION_PRODUCT_LIST_ACTION = "my_attention_product_list_action"
    }


    fun fetchMyProductListAction(token: String) {
        val userRepository = UserRepository()
        userRepository.fetchUserPushedProduct(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { products ->  store.dispatch(Action(MY_PRODUCT_LIST_ACTION, arrayOf(products))) }

    }

    fun fetchMyAttentionProductListAction(token: String) {
        val userRepository = UserRepository()
        userRepository.fetchUserAttentionProducts(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { products ->  store.dispatch(Action(MY_ATTENTION_PRODUCT_LIST_ACTION, arrayOf(products))) }

    }
}