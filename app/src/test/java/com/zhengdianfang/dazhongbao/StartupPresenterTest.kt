package com.zhengdianfang.dazhongbao

import com.zhengdianfang.dazhongbao.presenters.StartupPresenter
import com.zhengdianfang.dazhongbao.views.StartupView
import org.junit.Test
import org.mockito.Mockito
import org.junit.ClassRule

/**
 * Created by dfgzheng on 26/07/2017.
 */
class StartupPresenterTest {
    companion object {
        @ClassRule @JvmField val schedulers = RxImmediateSchedulerRule()
    }
    @Test
    fun loadData() {
        val startupPresenter = StartupPresenter()
        val startupView = Mockito.mock(StartupView::class.java)
        startupPresenter.attachView(startupView)
        startupPresenter.loadData()
    }

}