package com.zhengdianfang.dazhongbao.views.im.components.views

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.views.im.components.EmojiUtil

/**
 * Created by zheng on 16/6/29.
 */

class EmojiAdapter(private val mContext: Context, private val datas: List<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return datas.size
    }

    override fun getItem(position: Int): String {
        return datas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_face, parent, false)
        }
        val faceImage = convertView!!.findViewById<View>(R.id.item_iv_face) as ImageView
        val emoji = datas[position]
        if (emoji === EmojiUtil.EMOJI_DELETE_NAME) {
            faceImage.setImageResource(R.mipmap.deleteemoticonbtn)
        } else if (TextUtils.isEmpty(emoji)) {
            faceImage.setImageDrawable(null)
        } else {
            faceImage.tag = emoji
            faceImage.setImageResource(EmojiUtil.instance.mEmojis[emoji]!!)
        }
        return convertView
    }
}
