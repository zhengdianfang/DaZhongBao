package com.zhengdianfang.dazhongbao.views.setting

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.DateUtils
import com.zhengdianfang.dazhongbao.models.basic.Message

/**
 * Created by dfgzheng on 17/09/2017.
 */
class MessageItemAdapter(private val messages: MutableList<Message>) : RecyclerView.Adapter<MessageItemAdapter.MessageItemViewHolder>() {
    override fun onBindViewHolder(holder: MessageItemViewHolder?, position: Int) {
        holder?.setData(messages[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageItemViewHolder {
        return MessageItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.adapter_message_item_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    inner class MessageItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun setData(message: Message) {
            val messageTitleView = itemView?.findViewById<TextView>(R.id.messageTitleView)!!
            val messageContentView = itemView?.findViewById<TextView>(R.id.messageContentView)!!
            val messageTimeView = itemView?.findViewById<TextView>(R.id.messageTimeView)!!
            val messageImageView = itemView?.findViewById<ImageView>(R.id.messageImageView)!!
            messageTitleView.text = message.name
            messageContentView.text = message.desc
            messageTimeView.text = DateUtils.formatDateStr2Desc(System.currentTimeMillis(), message.ctime)
            if (message.banner?.isNullOrEmpty()!!){
                messageImageView.visibility = View.GONE
            }else{
                messageImageView.visibility = View.VISIBLE
                Glide.with(itemView?.context).load(message.banner).into(messageImageView)
            }
        }

    }
}