package com.zhengdianfang.dazhongbao.models.login

import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.UserApi
import com.zhengdianfang.dazhongbao.models.mock.mockSmsCode
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 31/07/2017.
 */
class LoginRepository {
    private val MOCK = true

    fun loginRequest(phoneNumber: String, password: String, version: String, deviceId: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).loginByPhone(phoneNumber, password, version, deviceId)
    }

    fun getSmsVerifyCode(phoneNumber: String, type: Int): Observable<String> {
        if (MOCK){
            return Observable.just(mockSmsCode).delay(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        return API.appClient.create(UserApi::class.java).getSmsVerifyCode(phoneNumber, type)
    }

    fun register(phoneNumber: String, verifyCode: String, recommendPerson: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).register(phoneNumber, verifyCode, recommendPerson)
    }

    fun modifyPassword(password: String, token: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).modifyPassword(password , "", "", token)

    }
}