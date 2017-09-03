package com.zhengdianfang.dazhongbao.views.im.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hyphenate.chat.EMMessage
import com.hyphenate.chat.EMTextMessageBody
import com.hyphenate.chat.EMVoiceMessageBody
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.im.components.EmojiUtil
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlin.properties.Delegates

/**
 * Created by dfgzheng on 03/09/2017.
 */
class ChatItemAdapter(private var receiverUser: User, private var messages: MutableList<EMMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val RECEIVER_TEXT_ITEM_TYPE = 0
    private val SEND_TEXT_ITEM_TYPE = 1
    private val RECEIVER_VOICE_ITEM_TYPE = 2
    private val SEND_VOICE_ITEM_TYPE = 2
    private val loginUser  = CApplication.INSTANCE.loginUser

    override fun getItemCount(): Int {
       return messages.count()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        when(getItemViewType(position)){
            SEND_TEXT_ITEM_TYPE, RECEIVER_TEXT_ITEM_TYPE -> {(holder as ChatTextItemViewHolder).setData(messages[position])}
            SEND_VOICE_ITEM_TYPE, RECEIVER_VOICE_ITEM_TYPE-> {(holder as ChatVoiceItemViewHolder).setData(messages[position])}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder{
        var viewHolder: RecyclerView.ViewHolder by Delegates.notNull()
        when(viewType){
            SEND_TEXT_ITEM_TYPE-> {viewHolder = ChatTextItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.chat_send_text_item_layout, parent, false)) }
            RECEIVER_TEXT_ITEM_TYPE -> {viewHolder = ChatTextItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.chat_receiver_text_item_layout, parent, false))}
            SEND_VOICE_ITEM_TYPE -> {viewHolder = ChatVoiceItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.chat_send_text_item_layout, parent, false)) }
            RECEIVER_VOICE_ITEM_TYPE -> {viewHolder = ChatVoiceItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.chat_receiver_text_item_layout, parent, false))}
        }
        return viewHolder
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        when(message.type){
            EMMessage.Type.TXT -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE){
                    RECEIVER_TEXT_ITEM_TYPE
                }else {
                    SEND_TEXT_ITEM_TYPE
                }
            }
            EMMessage.Type.VIDEO -> {
                return if (message.direct() == EMMessage.Direct.RECEIVE){
                    RECEIVER_VOICE_ITEM_TYPE
                }else {
                    SEND_VOICE_ITEM_TYPE
                }
            }
        }
        return SEND_TEXT_ITEM_TYPE
    }

    inner class ChatTextItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val userChatTextView by lazy { itemView?.findViewById<TextView>(R.id.userChatTextView)!! }
        private val userAvatarImageView by lazy { itemView?.findViewById<ImageView>(R.id.userAvatarImageView)!! }

        fun setData(emMessage: EMMessage) {
            val context = itemView?.context!!
            when(emMessage.type){
                EMMessage.Direct.RECEIVE -> {
                    Glide.with(context).load(receiverUser.avatar).bitmapTransform(CropCircleTransformation(context)).into(userAvatarImageView)
                }
                EMMessage.Direct.SEND -> {
                    Glide.with(context).load(loginUser?.avatar).bitmapTransform(CropCircleTransformation(context)).into(userAvatarImageView)
                }

            }
            userChatTextView.text = EmojiUtil.instance.addEmoji(context, (emMessage.body as EMTextMessageBody).message)
        }

    }
    inner class ChatVoiceItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val userChatTextView by lazy { itemView?.findViewById<TextView>(R.id.userChatTextView)!! }
        private val userAvatarImageView by lazy { itemView?.findViewById<ImageView>(R.id.userAvatarImageView)!! }

        fun setData(emMessage: EMMessage) {
            val context = itemView?.context!!
            when(emMessage.type){
                EMMessage.Direct.RECEIVE -> {
                    Glide.with(context).load(receiverUser.avatar).bitmapTransform(CropCircleTransformation(context)).into(userAvatarImageView)
                }
                EMMessage.Direct.SEND -> {
                    Glide.with(context).load(loginUser?.avatar).bitmapTransform(CropCircleTransformation(context)).into(userAvatarImageView)
                }
            }
            val voiceBody = emMessage.body as EMVoiceMessageBody
            userChatTextView.text = "${voiceBody.length}\""
        }
    }
}

