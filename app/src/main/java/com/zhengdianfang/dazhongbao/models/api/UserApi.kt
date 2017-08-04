package com.zhengdianfang.dazhongbao.models.api

import com.zhengdianfang.dazhongbao.models.login.User
import io.reactivex.Observable
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by dfgzheng on 26/07/2017.
 */
interface UserApi {

    @POST("/users/login")
    fun loginByPhone(@Query("username") username: String,
                     @Query("password") password: String,
                     @Query("ver") ver: String,
                     @Query("deviceid") deviceId: String,
                     @Query("platform") platform: String = "Android"): Observable<User>

    @POST("/users/sendvno")
    fun getSmsVerifyCode(@Query("phonenumber") phoneNumber: String,
                     @Query("ac") type: Int): Observable<String>

    @POST("/users/registered")
    fun register(@Query("phonenumber") phoneNumber: String,
                 @Query("verifyCode") verifyCode: String,
                 @Query("recommendPerson") recommendPerson: String): Observable<User>

    @POST("/users/resetpwd")
    fun modifyPassword(@Query("password") password: String,
                       @Query("phonenumber") phoneNumber: String,
                       @Query("verifyCode") verifyCode: String,
                       @Query("token") token: String): Observable<User>
}