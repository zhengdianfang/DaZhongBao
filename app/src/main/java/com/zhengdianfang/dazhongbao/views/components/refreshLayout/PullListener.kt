package com.zhengdianfang.dazhongbao.views.components.refreshLayout

/**
 * Created by dfgzheng on 05/08/2017.
 */
interface PullListener {

   fun  onPullingDown(refreshLayout: MiracleRefreshLayout, fraction: Float)

   fun onPullingUp(refreshLayout: MiracleRefreshLayout, fraction: Float)

   fun onPullDownReleae(refreshLayout: MiracleRefreshLayout, fraction: Float)

   fun onPUllUpRelase(refreshLayout: MiracleRefreshLayout, fraction: Float)

   fun onRefresh(refreshLayout: MiracleRefreshLayout)

   fun onLoadMore(refreshLayout: MiracleRefreshLayout)

   fun onFinishRefresh()

   fun onFinishLoadMore()

   fun onRefreshCanceled()

   fun onLoadMoreCanceled()
}