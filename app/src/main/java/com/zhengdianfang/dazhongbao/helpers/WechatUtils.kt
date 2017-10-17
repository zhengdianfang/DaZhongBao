package com.zhengdianfang.dazhongbao.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.zhengdianfang.dazhongbao.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.ExecutionException


/**
 * Created by dfgzheng on 16/09/2017.
 */
class WechatUtils(private val context: Context) {

    private val api by lazy { WXAPIFactory.createWXAPI(context, APP_ID); }
    companion object {
        val APP_ID = "wx40e80e1e92104c92"
        val APP_SERCET = "08c225ac497f5c469e183a559fc4a1aa"
    }

    fun getWeiXinToken() {
        api.registerApp(APP_ID)
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "carjob_wx_login"
        api.sendReq(req)
    }

    fun getWeiXinAccessToken(code: String, success: (accessToken: String, openId: String) -> Unit, fail: () -> Unit) {
        val client = Retrofit.Builder().baseUrl("https://api.weixin.qq.com/sns/")
                .addConverterFactory(JacksonConverterFactory.create()).build()
        val call = client.create(WeixinApi::class.java).getWenxinAccessTokenRequest(code)
        call.enqueue(object : Callback<Map<String, String>>{
            override fun onFailure(call: Call<Map<String, String>>?, t: Throwable?) {
                fail()
            }

            override fun onResponse(call: Call<Map<String, String>>?, response: Response<Map<String, String>>?) {
                success(response?.body()?.get("access_token") ?: "", response?.body()?.get("unionid") ?: "")
            }

        })
    }

    fun shareFriend(imagePath: String, title: String, desc: String, url: String) {
        Observable.create<Bitmap>({observer ->
            if (observer.isDisposed.not()){

                if (TextUtils.isEmpty(imagePath)) {
                    val drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
                    if (drawable is BitmapDrawable) {
                        observer.onNext(drawable.bitmap)
                        observer.onComplete()
                    }
                } else {
                    try {
                        val file = Glide.with(context).load(imagePath).downloadOnly(150, 150).get()
                        if (file != null && file.exists()) {
                            observer.onNext(BitmapFactory.decodeFile(file.absolutePath))
                            observer.onComplete()
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }

                }
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bitmap -> shareContent2Weixin(bitmap, title, desc, url, false) })
    }

    fun shareCirlce(imagePath: String, title: String, desc: String, url: String) {
        Observable.create<Bitmap>({observer ->
                if (TextUtils.isEmpty(imagePath)) {
                    val drawable = ContextCompat.getDrawable(context, R.mipmap.ic_launcher)
                    if (drawable is BitmapDrawable) {
                        observer.onNext(drawable.bitmap)
                        observer.onComplete()
                    }
                } else {
                    try {
                        val file = Glide.with(context).load(imagePath).downloadOnly(150, 150).get()
                        if (file != null && file.exists()) {
                            observer.onNext(BitmapFactory.decodeFile(file.absolutePath))
                            observer.onComplete()
                        }
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    } catch (e: ExecutionException) {
                        e.printStackTrace()
                    }

                }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe({ bitmap -> shareContent2Weixin(bitmap, title, desc, url, true) })
    }

    private fun shareContent2Weixin(bitmap: Bitmap?, title: String, desc: String, url: String, circle: Boolean) {
        val wxWebpageObject = WXWebpageObject()
        wxWebpageObject.webpageUrl = url
        val wxMediaMessage = WXMediaMessage(wxWebpageObject)
        wxMediaMessage.title = title
        wxMediaMessage.description = desc
        if (bitmap != null) {
            wxMediaMessage.setThumbImage(bitmap)
        }
        val req = SendMessageToWX.Req()
        req.message = wxMediaMessage
        req.scene = if (circle) SendMessageToWX.Req.WXSceneTimeline else SendMessageToWX.Req.WXSceneSession
        api.sendReq(req)
    }

    internal interface WeixinApi {

        @GET("oauth2/access_token")
        fun getWenxinAccessTokenRequest(@Query("code") code: String,
                                        @Query("appid") appid: String = APP_ID,
                                        @Query("secret") secret: String = APP_SERCET,
                                        @Query("grant_type") type: String = "authorization_code"): Call<Map<String, String>>

    }

}