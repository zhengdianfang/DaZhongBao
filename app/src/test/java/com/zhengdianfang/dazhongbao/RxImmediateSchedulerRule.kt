package com.zhengdianfang.dazhongbao

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 26/07/2017.
 */
class RxImmediateSchedulerRule: TestRule {
    private val immediate = object: Scheduler(){
        override fun scheduleDirect(run: Runnable?, delay: Long, unit: TimeUnit?): Disposable {
            return super.scheduleDirect(run, 0, unit)
        }

        override fun createWorker(): Worker {
           return ExecutorScheduler.ExecutorWorker(Runnable::run)
        }
    }
    override fun apply(base: Statement?, description: Description?): Statement {
        return object : Statement() {
            override fun evaluate() {
                RxJavaPlugins.setInitIoSchedulerHandler { scheduler -> immediate }
                RxJavaPlugins.setInitNewThreadSchedulerHandler{ scheduler -> immediate }
                RxJavaPlugins.setInitComputationSchedulerHandler{ scheduler -> immediate }
                RxJavaPlugins.setInitSingleSchedulerHandler{ scheduler -> immediate }
                RxAndroidPlugins.setInitMainThreadSchedulerHandler{ scheduler -> immediate }
                base?.evaluate()
                RxJavaPlugins.reset()
                RxAndroidPlugins.reset()
            }
        }
    }
}