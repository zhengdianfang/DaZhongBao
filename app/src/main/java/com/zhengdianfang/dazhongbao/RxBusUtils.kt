package com.zhengdianfang.dazhongbao

import com.zhengdianfang.dazhongbao.helpers.Action
import com.zhengdianfang.dazhongbao.helpers.RxBus
import com.zhengdianfang.dazhongbao.models.product.Product
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

/**
 * Created by dfgzheng on 26/08/2017.
 */
object RxBusUtils {

    fun registerFollowAndUnFollowProductActionsForRecyclerView(products: MutableList<Product>, uiCallback: (pos: Int) -> Unit): Disposable? {
        return RxBus.instance.register(actionTypes = arrayOf(Action.FOLLOW_PRODUCT_ACTION, Action.CANCEL_FOLLOW_PRODUCT_ACTION), consumer = Consumer { (type, data) ->
            val filters = products.filter { it.id == data}
            filters.forEach {
                when(type){
                    Action.FOLLOW_PRODUCT_ACTION -> it.attention = 1
                    Action.CANCEL_FOLLOW_PRODUCT_ACTION -> it.attention = 0
                }
                val pos = products.indexOf(it)
                uiCallback(pos)
            }
        })
    }
}