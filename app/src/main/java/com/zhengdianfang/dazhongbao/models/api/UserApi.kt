package com.zhengdianfang.dazhongbao.models.api

import com.zhengdianfang.dazhongbao.models.login.User
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * Created by dfgzheng on 26/07/2017.
 */
interface UserApi {
    companion object {
        val HOST = "http://10.38.87.87:11360"
    }

    @GET("/users/login")
    fun loginByPhone(phonenumber: String, password: String): Observable<User>
}