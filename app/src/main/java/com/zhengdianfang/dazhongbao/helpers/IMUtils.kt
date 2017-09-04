package com.zhengdianfang.dazhongbao.helpers

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Environment
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.BuildConfig
import com.zhengdianfang.dazhongbao.CApplication
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.basic.IMUser
import com.zhengdianfang.dazhongbao.models.login.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File




/**
 * Created by dfgzheng on 02/09/2017.
 */
object IMUtils {

    private var mediaPlayer: MediaPlayer? = null
    var isPlaying = false

    fun login(user: User): Observable<User> {
        return Observable.create<User> {ob->
           if (ob.isDisposed.not()){
               EMClient.getInstance().login(user.id, user.id, object: EMCallBack{
                   override fun onSuccess() {
                       ob.onNext(user)
                       Logger.d("emim login successful")
                   }

                   override fun onProgress(p0: Int, p1: String?) {
                   }

                   override fun onError(errorCode: Int, msg: String?) {
                       Logger.e("emim login error $errorCode :  $msg")
                       ob.onError(CException(msg ?: "", errorCode))
                   }

               })
           }
        }
    }

    fun register(user: User): Observable<User> {
        return Observable.create<User> {ob ->
           if (ob.isDisposed.not()){
               try {
                   EMClient.getInstance().createAccount(user.id, user.id)
                   ob.onNext(user)
               }catch (e: Exception){
                   ob.onError(e)
               }
           }
        }
    }

    fun getConversationList(): Observable<MutableList<EMConversation>>{
        return Observable.just({
            val conversations = EMClient.getInstance().chatManager().getConversationsByType(EMConversation.EMConversationType.Chat)
            conversations
        }.invoke()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getMessageList(userId: String):Observable<MutableList<EMMessage>>{
        return Observable.just({
            val conversation = EMClient.getInstance().chatManager().getConversation(userId, EMConversation.EMConversationType.Chat, true )
            conversation.markAllMessagesAsRead()
            var messages = mutableListOf<EMMessage>()
            Logger.d("im getMessage for userId : $userId and message size : ${conversation.allMsgCount}")
            if (conversation.allMsgCount > 0){
                val lastMessage = conversation.allMessages.last()
                messages = conversation.loadMoreMsgFromDB(lastMessage.msgId, conversation.allMsgCount)
                messages.add(lastMessage)
            }
            messages
        }.invoke()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getSoundCacheDirPath(context: Context): String {
        val soundDir = if (BuildConfig.DEBUG)
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "DaZhongBao/IM/Sound/"
        else
            context.cacheDir.toString() + File.separator + "IM/Sound/"
        val dir = File(soundDir)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return soundDir
    }

    fun getSoundCacheFilePath(context: Context, uuid: String): String {
        return if (BuildConfig.DEBUG)
            Environment.getExternalStorageDirectory().absolutePath + File.separator + "DaZhongBao/IM/Sound/" + uuid
        else
            context.cacheDir.absolutePath + File.separator + "IM/Sound/" + uuid
    }

    fun sendTxtMessage(user: IMUser, msg: String) {
        val message = EMMessage.createTxtSendMessage(msg, user.id)
        message.setAttribute("toUser", API.objectMapper.writeValueAsString(user))
        val loginUser = CApplication.INSTANCE.loginUser!!
        message.setAttribute("fromUser",
                API.objectMapper.writeValueAsString(IMUser(loginUser.id!!, loginUser.phonenumber!!, loginUser.id!!, loginUser.realname!!, loginUser.avatar!!)) )
        EMClient.getInstance().chatManager().sendMessage(message)
    }

    fun sendVoiceMessage(filePath: String, length: Int, user: IMUser){
        val message = EMMessage.createVoiceSendMessage(filePath, length, user.id)
        message.setAttribute("toUser", API.objectMapper.writeValueAsString(user))
        val loginUser = CApplication.INSTANCE.loginUser!!
        message.setAttribute("fromUser",
                API.objectMapper.writeValueAsString(IMUser(loginUser.id!!, loginUser.phonenumber!!, loginUser.id!!, loginUser.realname!!, loginUser.avatar!!)) )
        EMClient.getInstance().chatManager().sendMessage(message)
    }

    fun playVoice(context: Context, filePath: String, earpieceMode: Boolean = false) {
        if (!isPlaying && null == mediaPlayer){
            mediaPlayer = MediaPlayer()
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (earpieceMode){
                audioManager.isSpeakerphoneOn = false
                audioManager.mode = AudioManager.MODE_IN_CALL
                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_VOICE_CALL)
            }else{
                audioManager.mode = AudioManager.MODE_NORMAL
                audioManager.isSpeakerphoneOn = true
                mediaPlayer?.setAudioStreamType(AudioManager.STREAM_RING)
            }
            try {
                mediaPlayer?.setDataSource(filePath)
                mediaPlayer?.prepare()
                mediaPlayer?.setOnCompletionListener {
                    mediaPlayer?.release()
                    mediaPlayer = null
                    isPlaying = false

                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            isPlaying = true
            mediaPlayer?.start()
        }
    }

    fun stopVoice() {
        if (mediaPlayer != null && isPlaying) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            isPlaying = false
        }
    }
}