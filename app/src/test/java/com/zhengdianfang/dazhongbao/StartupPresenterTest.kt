package com.zhengdianfang.dazhongbao

import org.junit.ClassRule
import org.junit.Test

/**
 * Created by dfgzheng on 26/07/2017.
 */
class StartupPresenterTest {
    companion object {
        @ClassRule @JvmField val schedulers = RxImmediateSchedulerRule()
    }
    @Test
    fun loadData() {
    }

}