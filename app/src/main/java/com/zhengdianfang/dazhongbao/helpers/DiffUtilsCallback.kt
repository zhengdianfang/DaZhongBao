package com.zhengdianfang.dazhongbao.helpers

import android.support.v7.util.DiffUtil

/**
 * Created by zheng on 16/10/8.
 */

class DiffUtilsCallback<T>(private val newDatas: MutableList<T>, private val oldDatas: MutableList<T>,
                           private val mDiffUtilCompareListener: DiffUtilCompareListener<T>) : DiffUtil.Callback() {


    override fun getOldListSize(): Int {
        return oldDatas.size
    }

    override fun getNewListSize(): Int {
        return newDatas.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mDiffUtilCompareListener.areItemsTheSame(oldDatas[oldItemPosition], newDatas[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mDiffUtilCompareListener.areContentsTheSame(oldDatas[oldItemPosition], newDatas[newItemPosition])
    }
}
interface DiffUtilCompareListener<T> {
    fun areItemsTheSame(oldData: T, newData: T): Boolean
    fun areContentsTheSame(oldData: T, newData: T): Boolean
}
