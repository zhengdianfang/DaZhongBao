package com.zhengdianfang.dazhongbao.views.setting

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
import com.zhengdianfang.dazhongbao.views.im.ChatActivity

/**
 * Created by dfgzheng on 04/09/2017.
 */
class ChatConversationItemAdapter(private val conversations:MutableList<EMConversation>): RecyclerView.Adapter<ChatConversationItemAdapter.ChatConversationItemViewHolder>() {

    override fun getItemCount(): Int {
        return conversations.count()
    }

    override fun onBindViewHolder(holder: ChatConversationItemViewHolder?, position: Int) {
        holder?.setData(conversations[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ChatConversationItemViewHolder {
        return ChatConversationItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.adapter_chat_conversation_item_layout, parent, false))
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
}

