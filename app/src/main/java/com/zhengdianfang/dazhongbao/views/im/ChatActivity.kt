package com.zhengdianfang.dazhongbao.views.im

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.hyphenate.chat.EMMessage
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.presenters.ChatPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.im.adapter.ChatItemAdapter

/**
 * Created by dfgzheng on 03/09/2017.
 */
class ChatActivity : BaseActivity(), ChatPresenter.IMUserInfoAndMessages{

    private val messageRecyclerView by lazy { findViewById<RecyclerView>(R.id.messageRecyclerView) }
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val chatPresenter = ChatPresenter()
    private val userId by lazy { intent.getStringExtra("userId") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatPresenter.attachView(this)

        toolBar.backListener = {
          onBackPressed()
        }
        chatPresenter.fetchChatList(userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatPresenter.detachView()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun receiverUserInfoAndMessages(user: User, allMessage: MutableList<EMMessage>) {
        toolBar.setTitle(user.realname ?: "")
        messageRecyclerView.adapter = ChatItemAdapter(user, allMessage)
    }
}