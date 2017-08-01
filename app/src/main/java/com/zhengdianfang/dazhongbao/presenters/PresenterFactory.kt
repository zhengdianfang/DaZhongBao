package com.zhengdianfang.dazhongbao.presenters

/**
 * Created by dfgzheng on 31/07/2017.
 */
object PresenterFactory {
    private val presenterList = mutableMapOf<String,BasePresenter>()
    val mLoginPresenter by lazy {
        val presenter = LoginPresenter()
        presenterList.put(LoginPresenter::class.java.simpleName, presenter)
        presenter
    }

    fun destory() {
        presenterList.forEach {
            it.value.detachView()
        }
        presenterList.clear()
    }

}