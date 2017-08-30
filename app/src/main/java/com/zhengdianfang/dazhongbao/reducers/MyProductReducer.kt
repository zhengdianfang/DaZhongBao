package com.zhengdianfang.dazhongbao.reducers

import com.miracle.redux.Action
import com.miracle.redux.Reducer
import com.miracle.redux.Store
import com.zhengdianfang.dazhongbao.actions.MyProductAction
import com.zhengdianfang.dazhongbao.models.product.Product

/**
 * Created by dfgzheng on 28/08/2017.
 */
class MyProductReducer: Reducer<AppState> {
    override fun reduce(state: AppState?, action: Action?): AppState? {
        when (action?.type) {
            Store.INIT_ACTION -> {

            }
            MyProductAction.MY_PRODUCT_LIST_ACTION -> {
                state?.myProductList?.clear()
                state?.myProductList?.addAll(action.values[0] as MutableList<Product>)
                return state
            }
            MyProductAction.MY_ATTENTION_PRODUCT_LIST_ACTION-> {
                state?.myAnttentionProductList?.clear()
                state?.myAnttentionProductList?.addAll(action.values[0] as MutableList<Product>)
                return state
            }
        }
        return state
    }
}

