package com.zhengdianfang.dazhongbao.models.login

import com.zhengdianfang.dazhongbao.helpers.Constants
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.CException
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
class LoginRepository(private val MOCK: Boolean = Constants.MOCK) {

    fun loginRequest(phoneNumber: String, password: String, version: String, deviceId: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).loginByPhone(phoneNumber, password, version, deviceId)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun getSmsVerifyCode(phoneNumber: String, type: Int): Observable<String> {
        if (MOCK){
            return Observable.just(mockSmsCode).delay(2, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
        return API.appClient.create(UserApi::class.java).getSmsVerifyCode(phoneNumber, type)
                .switchMap{ json ->
                    if(json.has("vno")) {
                        return@switchMap Observable.just(json.get("vno").asText())
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                 }
    }

    fun register(phoneNumber: String, verifyCode: String, recommendPerson: String): Observable<User> {
        if (MOCK) {
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).register(phoneNumber, verifyCode, recommendPerson)
                     .map {response -> API.parseResponse(response) }
                     .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

}