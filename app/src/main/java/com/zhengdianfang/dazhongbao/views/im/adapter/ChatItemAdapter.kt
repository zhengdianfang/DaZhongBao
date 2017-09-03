package com.zhengdianfang.dazhongbao.views.im.adapter

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.views.im.components.EmojiUtil
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlin.properties.Delegates

/**
 * Created by dfgzheng on 03/09/2017.
 */
class ChatItemAdapter(private var context: Context, private var receiverUser: User, private var messages: MutableList<EMMessage>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), SensorEventListener {

    private val RECEIVER_TEXT_ITEM_TYPE = 0
    private val SEND_TEXT_ITEM_TYPE = 1
    private val RECEIVER_VOICE_ITEM_TYPE = 2
    private val SEND_VOICE_ITEM_TYPE = 2
    private val loginUser  = CApplication.INSTANCE.loginUser
    private val sensorManager by lazy { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val sensor by lazy { sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY) }
    private var earpieceMode = false

    init {
        sensorManager.registerListener(this, sensor,  SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun destory() {
        sensorManager.unregisterListener(this)
    }

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
            SEND_VOICE_ITEM_TYPE -> {viewHolder = ChatVoiceItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.chat_send_voice_item_layout, parent, false)) }
            RECEIVER_VOICE_ITEM_TYPE -> {viewHolder = ChatVoiceItemViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.chat_receiver_voice_item_layout, parent, false))}
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
            EMMessage.Type.VOICE-> {
                return if (message.direct() == EMMessage.Direct.RECEIVE){
                    RECEIVER_VOICE_ITEM_TYPE
                }else {
                    SEND_VOICE_ITEM_TYPE
                }
            }
        }
        return SEND_TEXT_ITEM_TYPE
    }

    fun updateMessages(messages: MutableList<EMMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    fun addMessages(messages: MutableList<EMMessage>) {
        this.messages.addAll(messages)
        notifyDataSetChanged()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val range = event?.values?.get(0)
        earpieceMode = range != sensor.maximumRange
    }

    inner class ChatTextItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val userChatTextView by lazy { itemView?.findViewById<TextView>(R.id.userChatTextView)!! }
        private val userAvatarImageView by lazy { itemView?.findViewById<ImageView>(R.id.userAvatarImageView)!! }

        fun setData(emMessage: EMMessage) {
            val context = itemView?.context!!
            when(emMessage.direct()){
                EMMessage.Direct.RECEIVE -> {
                    Glide.with(context).load(receiverUser.avatar)
                            .bitmapTransform(CropCircleTransformation(context))
                            .placeholder(R.mipmap.fragment_personal_default_header_image)
                            .error(R.mipmap.fragment_personal_default_header_image)
                            .into(userAvatarImageView)
                }
                EMMessage.Direct.SEND -> {
                    Glide.with(context).load(loginUser?.avatar)
                            .bitmapTransform(CropCircleTransformation(context))
                            .placeholder(R.mipmap.fragment_personal_default_header_image)
                            .error(R.mipmap.fragment_personal_default_header_image)
                            .into(userAvatarImageView)
                }

            }
            userChatTextView.text = EmojiUtil.instance.getSpannableString(context, (emMessage.body as EMTextMessageBody).message)
        }

    }
    inner class ChatVoiceItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val userChatTextView by lazy { itemView?.findViewById<TextView>(R.id.userChatTextView)!! }
        private val userAvatarImageView by lazy { itemView?.findViewById<ImageView>(R.id.userAvatarImageView)!! }

        fun setData(emMessage: EMMessage) {
            val context = itemView?.context!!
            when(emMessage.direct()){
                EMMessage.Direct.RECEIVE -> {
                    Glide.with(context).load(receiverUser.avatar)
                            .bitmapTransform(CropCircleTransformation(context))
                            .placeholder(R.mipmap.fragment_personal_default_header_image)
                            .error(R.mipmap.fragment_personal_default_header_image)
                            .into(userAvatarImageView)
                }
                EMMessage.Direct.SEND -> {
                    Glide.with(context).load(loginUser?.avatar)
                            .bitmapTransform(CropCircleTransformation(context))
                            .placeholder(R.mipmap.fragment_personal_default_header_image)
                            .error(R.mipmap.fragment_personal_default_header_image)
                            .into(userAvatarImageView)
                }
            }
            val voiceBody = emMessage.body as EMVoiceMessageBody
            userChatTextView.text = "${voiceBody.length}\""

            itemView?.setOnClickListener {
                if (IMUtils.isPlaying) {
                    IMUtils.stopVoice()
                }
                IMUtils.playVoice(context, voiceBody.localUrl, earpieceMode)
            }
        }
    }
}

