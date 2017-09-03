package com.zhengdianfang.dazhongbao.helpers

import android.content.Context
import android.os.Environment
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMConversation
import com.hyphenate.chat.EMMessage
import com.orhanobut.logger.Logger
import com.zhengdianfang.dazhongbao.BuildConfig
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.login.User
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.File


/**
 * Created by dfgzheng on 02/09/2017.
 */
object IMUtils {

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

    fun getMessageList(userId: String):Observable<MutableList<EMMessage>>{
        Logger.d("im getMessage for userId : $userId")
        return Observable.just({
            val conversation = EMClient.getInstance().chatManager().getConversation(userId, EMConversation.EMConversationType.Chat, true )
            conversation.markAllMessagesAsRead()
            conversation.allMessages
        }.invoke()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getSoundChaceDirPath(context: Context): String {
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
            Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "DaZhongBao/IM/Sound/" + uuid
        else
            context.cacheDir.absolutePath + File.separator + "IM/Sound/" + uuid
    }

}