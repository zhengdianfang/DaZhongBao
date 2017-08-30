package com.zhengdianfang.dazhongbao.actions

import com.miracle.redux.Action

/**
 * Created by dfgzheng on 30/08/2017.
 */
class BaseAction(type: String, var presistance: Boolean = false) : Action(type) {
}