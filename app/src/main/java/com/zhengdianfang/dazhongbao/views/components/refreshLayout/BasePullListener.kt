package com.zhengdianfang.dazhongbao.views.components.refreshLayout

/**
 * Created by dfgzheng on 05/08/2017.
 */
abstract class BasePullListener : PullListener{
    override fun onPullingDown(refreshLayout: MiracleRefreshLayout, fraction: Float) {
    }

    override fun onPullingUp(refreshLayout: MiracleRefreshLayout, fraction: Float) {
    }

    override fun onPullDownReleae(refreshLayout: MiracleRefreshLayout, fraction: Float) {
    }

    override fun onPUllUpRelase(refreshLayout: MiracleRefreshLayout, fraction: Float) {
    }

    override fun onRefresh(refreshLayout: MiracleRefreshLayout) {
    }

    override fun onLoadMore(refreshLayout: MiracleRefreshLayout) {
    }

    override fun onFinishRefresh() {
    }

    override fun onFinishLoadMore() {
    }

    override fun onRefreshCanceled() {
    }

    override fun onLoadMoreCanceled() {
    }
}