package com.zhengdianfang.dazhongbao.models.login

import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.UserApi
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 31/07/2017.
 */
class LoginRepository {
    private val MOCK = true

    fun loginRequest(phoneNumber: String, password: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).loginByPhone(phoneNumber, password)
    }
}