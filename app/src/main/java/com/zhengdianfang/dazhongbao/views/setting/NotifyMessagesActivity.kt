package com.zhengdianfang.dazhongbao.views.setting

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.hyphenate.chat.EMConversation
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.basic.MessageCount
import com.zhengdianfang.dazhongbao.presenters.NotifyMessagePresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseListActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar

class NotifyMessagesActivity : BaseListActivity<EMConversation>(), NotifyMessagePresenter.INofiyMessageCountAndConversatonsList{

    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val notifyMessageRecyclerView by lazy { findViewById<RecyclerView>(R.id.notifyMessageRecyclerView) }
    private val notifyMessagePresenter = NotifyMessagePresenter()
    private val messages = mutableListOf<MessageCount>()
    private val conversations = mutableListOf<EMConversation>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notify_messages)
        notifyMessagePresenter.attachView(this)
        setupRecyclerView()
        toolBar.backListener = {
           onBackPressed()
        }
        autoRefresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        notifyMessagePresenter.detachView()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun requestList(pageNumber: Int) {
        notifyMessagePresenter.fetchNotifyMessageCount(CApplication.INSTANCE.loginUser?.token!!)
    }

    override fun createRecyclerView(): RecyclerView {
       return notifyMessageRecyclerView
    }

    override fun createRecyclerViewAdapter(): RecyclerView.Adapter<*> {
        return NotifyMessageItemAdapter(messages, conversations)
    }

    override fun receiverList(messageCounts: MutableList<MessageCount>, conversations: MutableList<EMConversation>) {
        this.messages.clear()
        this.messages.addAll(messageCounts)
        this.conversations.clear()
        this.conversations.addAll(conversations)
        adapter.notifyDataSetChanged()
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadmore()
    }

}
