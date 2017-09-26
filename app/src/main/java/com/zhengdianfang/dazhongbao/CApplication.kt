package com.zhengdianfang.dazhongbao

import android.app.Application
import android.content.Intent
import android.os.Environment
import android.preference.PreferenceManager
import android.text.TextUtils
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.umeng.message.IUmengRegisterCallback
import com.umeng.message.PushAgent
import com.zhengdianfang.dazhongbao.helpers.FileUtils
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.cache.BaseMemoryCache
import com.zhengdianfang.dazhongbao.models.cache.BasicDiskCache
import com.zhengdianfang.dazhongbao.models.login.User
import com.zhengdianfang.dazhongbao.models.product.SharesInfo
import com.zhengdianfang.dazhongbao.views.login.LoginActivity
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.File
import kotlin.properties.Delegates


/**
 * Created by dfgzheng on 30/07/2017.
 */
class CApplication : Application(){
    companion object {
        var INSTANCE: CApplication by Delegates.notNull()
    }

    private val preference by lazy { PreferenceManager.getDefaultSharedPreferences(this) }

    var loginUser: User? = null
        set(value) {
            if (value == null){
                preference.edit().remove("login_user").commit()
            }else{
                preference.edit().putString("login_user", API.objectMapper.writeValueAsString(value)).commit()
            }
            field = value
        }
        get() {
            if (field == null) {
                val json = preference.getString("login_user", "")
                if (!TextUtils.isEmpty(json)){
                    field = API.objectMapper.readValue<User>(json, User::class.java)
                }
            }
            return field
        }

    val shareInfosCache = mutableListOf<SharesInfo>()

    val diskCahce by lazy {  BasicDiskCache.fromCtx(this) }
    val memoryCache by lazy {  BaseMemoryCache.fromCtx(this) }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        val appDir = File(Environment.getExternalStorageDirectory().absolutePath + File.separator + FileUtils.APP_DIR)
        if (appDir.exists().not()) {
            appDir.mkdirs()
        }
        Logger.addLogAdapter(object : AndroidLogAdapter() {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
        initEMChat()
        initSmartLayoutConfiguration()
        registerPushSDK()
    }

    private fun registerPushSDK() {
        val mPushAgent = PushAgent.getInstance(this)
        mPushAgent.setDebugMode(BuildConfig.DEBUG)
        //注册推送服务，每次调用register方法都会回调该接口
        Observable.create<String> {observer ->
            mPushAgent.register(object : IUmengRegisterCallback {

                override fun onSuccess(deviceToken: String) {
                    //注册成功会返回device token
                    observer.onNext(deviceToken)
                    observer.onComplete()
                }

                override fun onFailure(s: String, s1: String) {
                    Logger.e("umeng push sdk register fail: $s ,  $s1")
                    observer.onComplete()
                }
            })
        }.subscribeOn(Schedulers.newThread()).subscribe {token ->
           Logger.d("umeng push sdk register success, token is : $token")
        }
    }

    private fun initSmartLayoutConfiguration() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreater { context, layout ->
            layout.setPrimaryColorsId(R.color.white, android.R.color.black)
            ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreater { context, layout ->
            ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)
        }
    }

    fun isLogin(): Boolean {
       return loginUser != null
    }

    private fun initEMChat() {
        val emOptions = EMOptions()
        emOptions.acceptInvitationAlways = false
        EMClient.getInstance().init(this, emOptions)
        EMClient.getInstance().setDebugMode(BuildConfig.DEBUG)
    }

    fun logout() {
        loginUser = null
        EMClient.getInstance().logout(true)
        startActivity(Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }
}