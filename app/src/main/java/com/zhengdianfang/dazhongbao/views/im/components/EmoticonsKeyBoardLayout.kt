package com.zhengdianfang.dazhongbao.views.im.components

import android.Manifest
import android.app.Activity
import android.content.Context
import android.text.SpannableString
import android.text.TextUtils
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhengdianfang.dazhongbao.R
import com.zhengdianfang.dazhongbao.helpers.IMUtils
import com.zhengdianfang.dazhongbao.views.im.components.views.AutoHeightLayout
import com.zhengdianfang.dazhongbao.views.im.components.views.EmojiViewPager


/**
 * Created by zheng on 16/7/6.
 */

class EmoticonsKeyBoardLayout(context: Context, attrs: AttributeSet) : AutoHeightLayout(context, attrs), FuncLayout.OnFuncChangeListener, EmoticonsEditText.OnBackKeyClickListener {


    private val mSwitchVoiceBtn by lazy { findViewById<ImageView>(R.id.swtichVoiceBtn) }
    val mVoiceBtn by lazy { findViewById<RecordButton>(R.id.voiceBtn) }
    val mChatEdit by lazy { findViewById<EmoticonsEditText>(R.id.chatEdit) }
    private val mFaceBtn by lazy { findViewById<ImageView>(R.id.faceBtn) }
    private val mInputFrameLayout by lazy { findViewById<ViewGroup>(R.id.inputFrameLayout) }
    private val mFunsLayout by lazy { findViewById<FuncLayout>(R.id.funsLayout) }
    var mOnSoftKeyStateListener: OnSoftKeyStateListener? = null

    interface OnSoftKeyStateListener {
        fun onSoftKeyPop()
    }


    private var mDispatchKeyEventPreImeLock = false


    init {
        LayoutInflater.from(context).inflate(R.layout.emoticons_keyboard_layout, this)
        initViews(context)
        initViewpager(context)
        mVoiceBtn.setSavePath(IMUtils.getSoundCacheDirPath(context))

    }

    private fun initViews(context: Context) {

        mFaceBtn.setOnClickListener { v -> toggleFuncView(EMOJI_FUN_KEY) }
        mChatEdit.mOnBackKeyClickListener = this
        mChatEdit.setOnTouchListener { _, _ ->
            if (!mChatEdit.isFocused) {
                mChatEdit.isFocusable = true
                mChatEdit.isFocusableInTouchMode = true
            }
            false
        }


        mSwitchVoiceBtn.setOnClickListener {
            if (mInputFrameLayout.isShown) {
                RxPermissions(context as Activity).request(Manifest.permission.RECORD_AUDIO)
                        .subscribe({ granted ->
                            if (granted) {
                                mSwitchVoiceBtn.setImageResource(R.drawable.voice_icon_selected)
                                showVoice()
                            } else {
                                Toast.makeText(context, R.string.please_setting_voice_premission, Toast.LENGTH_SHORT).show()
                            }
                        })
            } else {
                showText()
                mSwitchVoiceBtn.setImageResource(R.drawable.voice_icon_normal)
                KeyboardUtils.openSoftKeyboard(mChatEdit)
            }
        }
    }


    private fun showVoice() {
        mInputFrameLayout.visibility = View.GONE
        mVoiceBtn.visibility = View.VISIBLE
        reset()
    }

    private fun showText() {
        mInputFrameLayout.visibility = View.VISIBLE
        mVoiceBtn.visibility = View.GONE
    }

    private fun initViewpager(context: Context) {
        val emojiViewPager = LayoutInflater.from(context).inflate(R.layout.emoji_layout, null, false) as EmojiViewPager
        emojiViewPager.mAddEmojiListener = object : EmojiViewPager.AddEmojiListener {
            override fun addEmojiEvent(spannableString: SpannableString) {
                mChatEdit.append(spannableString)
            }

            override fun deleteEmojiEvent() {
                if (!TextUtils.isEmpty(mChatEdit.text.toString())) {
                    val selection = mChatEdit.selectionStart
                    val strInputText = mChatEdit.text.toString()
                    if (selection > 0) {
                        val strText = strInputText.substring(selection - 1)
                        if (TextUtils.equals("]", strText)) {
                            val start = strInputText.lastIndexOf("[")
                            mChatEdit.text.delete(start, selection)
                        } else {
                            mChatEdit.text.delete(selection - 1, selection)
                        }
                    }
                }
            }
        }
        mFunsLayout.addFuncView(EMOJI_FUN_KEY, emojiViewPager)

    }

    fun toggleFuncView(key: Int) {
        mFunsLayout.toggleFuncView(key, isSoftKeyboardPop, mChatEdit)
    }

    override fun OnSoftPop(height: Int) {
        super.OnSoftPop(height)
        mFunsLayout.setMVisibility(true)
        onFuncChange(0)
        mOnSoftKeyStateListener?.onSoftKeyPop()
    }

    override fun OnSoftClose() {
        super.OnSoftClose()
        if (mFunsLayout.isOnlyShowSoftKeyboard) {
            reset()
        } else {
            onFuncChange(mFunsLayout.mCurrentFuncKey)
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (mDispatchKeyEventPreImeLock) {
                mDispatchKeyEventPreImeLock = false
                return true
            }
            return if (mFunsLayout.isShown) {
                reset()
                true
            } else {
                super.dispatchKeyEvent(event)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onBackKeyClick() {
        if (mFunsLayout.isShown) {
            mDispatchKeyEventPreImeLock = true
            reset()
        }
    }

    override fun onFuncChange(key: Int) {
        if (EMOJI_FUN_KEY == key) {
            mFaceBtn.setImageResource(R.drawable.icon_emoji_selected)
        } else {
            mFaceBtn.setImageResource(R.drawable.icon_emoji_normal)
        }
        checkVoiceState()
    }


    private fun checkVoiceState() {
        if (mVoiceBtn.isShown) {
            mSwitchVoiceBtn!!.setImageResource(R.drawable.voice_icon_selected)
        } else {
            mSwitchVoiceBtn!!.setImageResource(R.drawable.voice_icon_normal)
        }
    }

    override fun onSoftKeyboardHeightChanged(height: Int) {
        mFunsLayout.mHeight = height
    }

    fun reset() {
        KeyboardUtils.closeSoftKeyboard(mContext as Activity)
        mFunsLayout.hideAllFuncView()
        mFaceBtn.setImageResource(R.drawable.icon_emoji_normal)
    }

    companion object {
        val EMOJI_FUN_KEY = 1
        val OTHER_FUN_KEY = 2
    }
}
