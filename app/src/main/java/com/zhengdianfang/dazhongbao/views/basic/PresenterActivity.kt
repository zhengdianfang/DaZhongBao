package com.zhengdianfang.dazhongbao.views.basic

import android.os.Bundle
import com.zhengdianfang.dazhongbao.presenters.IPresenter

/**
 * Created by dfgzheng on 25/07/2017.
 */
abstract class PresenterActivity<V: IView<M>, P : IPresenter<M, V>, M> : BaseActivity() {

    protected var mPersenter: P? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPersenter = createPresenter()
    }

    abstract fun createPresenter() : P?

    override fun onDestroy() {
        super.onDestroy()
        mPersenter?.detachView()
    }
}