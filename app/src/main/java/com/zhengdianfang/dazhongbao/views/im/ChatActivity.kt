package com.zhengdianfang.dazhongbao.views.im

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.EditorInfo
import com.hyphenate.chat.EMMessage
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.basic.IMUser
import com.zhengdianfang.dazhongbao.presenters.ChatPresenter
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.components.Toolbar
import com.zhengdianfang.dazhongbao.views.im.adapter.ChatItemAdapter
import com.zhengdianfang.dazhongbao.views.im.components.EmoticonsKeyBoardLayout
import com.zhengdianfang.dazhongbao.views.im.components.RecordButton

/**
 * Created by dfgzheng on 03/09/2017.
 */
class ChatActivity : BaseActivity(), ChatPresenter.IMUserInfoAndMessages{

    private val messageRecyclerView by lazy { findViewById<RecyclerView>(R.id.messageRecyclerView) }
    private val emoticonContainer by lazy { findViewById<EmoticonsKeyBoardLayout>(R.id.emoticonContainer) }
    private val toolBar by lazy { findViewById<Toolbar>(R.id.toolbar) }
    private val chatPresenter = ChatPresenter()
    private val user by lazy { API.objectMapper.readValue(intent.getStringExtra("user"), IMUser::class.java) }
    private var chatItemAdapter: ChatItemAdapter? = null

    companion object {
        fun startActivity(context: Context, imUser: IMUser?) {
           context.startActivity(Intent(context, ChatActivity::class.java).putExtra("user", API.objectMapper.writeValueAsString(imUser)))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatPresenter.attachView(this)

        initViews()
        chatPresenter.fetchChatList(user.id!!)
    }

    private fun initViews() {
        toolBar.backListener = {
            onBackPressed()
        }
        emoticonContainer.mChatEdit.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                chatPresenter.sendTextMessage(user, textView.text.toString())
                textView.text = ""
            }
            false
        }

        emoticonContainer.mVoiceBtn.setOnFinishedRecordListener(object : RecordButton.OnFinishedRecordListener {
            override fun onFinishedRecord(audioPath: String, time: Int) {
                chatPresenter.sendVoiceMessage(user, audioPath, time)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        chatItemAdapter?.destory()
        chatPresenter.detachView()
    }

    override fun onBackPressed() {
        finish()
    }


    override fun receiverMessages(allMessage: MutableList<EMMessage>) {
        Logger.d("receiver all messages size : ${allMessage.count()}")
        toolBar.setTitle(user.realname ?: "")
        chatItemAdapter = ChatItemAdapter(this, user, allMessage)
        messageRecyclerView.adapter = chatItemAdapter
        scrollToEnd()
    }

    override fun updateMessages(messages: MutableList<EMMessage>) {
        if (chatItemAdapter != null) {
            chatItemAdapter!!.updateMessages(messages)
            scrollToEnd()
        }
    }

    override fun addMessages(messages: MutableList<EMMessage>) {
        if (chatItemAdapter != null) {
            chatItemAdapter!!.addMessages(messages)
        }
        scrollToEnd()
    }

    private fun scrollToEnd() {
        messageRecyclerView.scrollToPosition(chatItemAdapter!!.itemCount - 1)
    }
}