package com.zhengdianfang.dazhongbao.views.setting

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.chat.EMVoiceMessageBody
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.basic.IMUser
import com.zhengdianfang.dazhongbao.models.basic.MessageCount
import com.zhengdianfang.dazhongbao.views.basic.BaseActivity
import com.zhengdianfang.dazhongbao.views.im.ChatActivity

/**
 * Created by dfgzheng on 04/09/2017.
 */
class NotifyMessageItemAdapter(private val messages: MutableList<MessageCount>, private val conversations:MutableList<EMConversation>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val MESSAGE_ITEM = 0
    private val CONVERSATION_ITEM = 1

    override fun getItemCount(): Int {
        return messages.count() + conversations.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(getItemViewType(position)){
            MESSAGE_ITEM -> { (holder as MessageItemViewHolder).setData(messages[position]) }
            CONVERSATION_ITEM -> { (holder as ChatConversationItemViewHolder).setData(conversations[position]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder{
        var  viewHolder: RecyclerView.ViewHolder? = null
        when(viewType){
            MESSAGE_ITEM -> {
                viewHolder = MessageItemViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.adapter_auction_start_header_layout, parent, false))
            }
            CONVERSATION_ITEM -> {
                viewHolder = ChatConversationItemViewHolder(LayoutInflater.from(parent?.context)
                        .inflate(R.layout.adapter_chat_conversation_item_layout, parent, false))
            }
        }
        return viewHolder!!
    }

    override fun getItemViewType(position: Int): Int {
        if (position < messages.count()) {
           return MESSAGE_ITEM
        }
        return CONVERSATION_ITEM
    }

    inner class ChatConversationItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val headerImageView by lazy { itemView?.findViewById<ImageView>(R.id.userHeaderView)!! }
        private val userNameTextView by lazy { itemView?.findViewById<TextView>(R.id.userNameTextView)!! }
        private val contentView by lazy { itemView?.findViewById<TextView>(R.id.contentView)!! }
        fun setData(conversation: EMConversation){
            val context = itemView?.context!!
            val lastMessage = conversation.lastMessage
            userNameTextView.text = lastMessage.from
            val body = lastMessage.body
            if (body is EMTextMessageBody){
                contentView.text = body.message
            }else if (body is EMVoiceMessageBody){
                contentView.text =  itemView?.context?.getString(R.string.voice_content_sample)
            }
            val otherJson = if (conversation.latestMessageFromOthers != null) {
                conversation.latestMessageFromOthers.getStringAttribute("fromUser", "")
            }else{
                conversation.lastMessage.getStringAttribute("toUser", "")
            }
            if (!TextUtils.isEmpty(otherJson)){
                val otherUser = API.objectMapper.readValue<IMUser>(otherJson, IMUser::class.java)
                Glide.with(context).load(otherUser.avatar)
                        .placeholder(R.mipmap.fragment_personal_default_header_image)
                        .error(R.mipmap.fragment_personal_default_header_image)
                        .into(headerImageView)
                userNameTextView.text = otherUser.realname
                itemView?.setOnClickListener {
                    val otherUser = API.objectMapper.readValue<IMUser>(otherJson, IMUser::class.java)
                    ChatActivity.startActivity(context, otherUser)
                }
            }else{
                Glide.with(context).load(R.mipmap.fragment_personal_default_header_image).into(headerImageView)
            }
        }
    }

    inner class MessageItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun setData(messageCount: MessageCount){
            val notifyMessageTitleView = itemView?.findViewById<TextView>(R.id.nofiyMessageTitleView)!!
            val notifyMessageDetailView = itemView?.findViewById<TextView>(R.id.nofityMessageDetailView)!!
            val badgeView = itemView?.findViewById<View>(R.id.badgeView)
            badgeView.visibility = if (messageCount.gcount == 0)  View.GONE else View.VISIBLE
            notifyMessageTitleView.text = messageCount.name
            notifyMessageDetailView.text = messageCount.message
            itemView?.setOnClickListener {
                if(itemView?.context is BaseActivity) {
                    val fragment = MessageListByTypeFragment()
                    val bundle = Bundle()
                    bundle.putInt("iconType", messageCount.iconType)
                    bundle.putString("title", messageCount.name)
                    fragment.arguments = bundle
                    (itemView?.context as BaseActivity).startFragment(android.R.id.content, fragment, "message")
                }
            }
        }

    }
}

