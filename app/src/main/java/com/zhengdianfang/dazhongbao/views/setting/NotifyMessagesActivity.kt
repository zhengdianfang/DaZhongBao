package com.zhengdianfang.dazhongbao.views.setting

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.widget.TextView
import com.hyphenate.chat.EMConversation
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.basic.GMessageCount
import com.zhengdianfang.dazhongbao.models.basic.MessageCount
import com.zhengdianfang.dazhongbao.presenters.NotifyMessagePresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar

class NotifyMessagesActivity : BaseListActivity<EMConversation>(), NotifyMessagePresenter.INofiyMessageCountAndConversatonsList{

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val notifyMessageRecyclerView by lazy { findViewById<XRecyclerView>(R.id.notifyMessageRecyclerView) }
    private val auctionStartHeaderView by lazy { LayoutInflater.from(this)
            .inflate(R.layout.adapter_auction_start_header_layout, notifyMessageRecyclerView, false ) }
    private val auctionSuccessHeaderView by lazy { LayoutInflater.from(this)
            .inflate(R.layout.adapter_auction_success_header_layout, notifyMessageRecyclerView, false ) }
    private val notifyMessagePresenter = NotifyMessagePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_messages)
        notifyMessagePresenter.attachView(this)
        setupRecyclerView()
        toolBar.backListener = {
           onBackPressed()
        }
        notifyMessageRecyclerView.addHeaderView(auctionStartHeaderView)
        notifyMessageRecyclerView.addHeaderView(auctionSuccessHeaderView)
        recyclerView.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        notifyMessagePresenter.detachView()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun requestList(pageNumber: Int) {
        notifyMessagePresenter.fetchNotifyMessageCount(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun createRecyclerView(): XRecyclerView {
       return notifyMessageRecyclerView
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return ChatConversationItemAdapter(datas)
    }

    override fun receiverList(gMessageCount: GMessageCount, conversations: MutableList<EMConversation>) {
        initAuctionStartItemViews(gMessageCount.Message2)
        initAuctionSuccessItemViews(gMessageCount.Message3)
        reponseProcessor(conversations)
    }

    private fun initAuctionStartItemViews(messageCount: MessageCount) {
        val notifyMessageTitleView = auctionStartHeaderView.findViewById<TextView>(R.id.nofiyMessageTitleView)
        val notifyMessageDetailView = auctionStartHeaderView.findViewById<TextView>(R.id.nofityMessageDetailView)
        notifyMessageTitleView.text = messageCount.name
        notifyMessageDetailView.text = messageCount.message
    }
    private fun initAuctionSuccessItemViews(messageCount: MessageCount) {
        val notifyMessageTitleView = auctionSuccessHeaderView.findViewById<TextView>(R.id.nofiyMessageTitleView)
        val notifyMessageDetailView = auctionSuccessHeaderView.findViewById<TextView>(R.id.nofityMessageDetailView)
        notifyMessageTitleView.text = messageCount.name
        notifyMessageDetailView.text = messageCount.message
    }
}
