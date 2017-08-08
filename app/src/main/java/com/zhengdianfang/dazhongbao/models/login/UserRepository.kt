package com.zhengdianfang.dazhongbao.models.login

import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.api.UserApi
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 08/08/2017.
 */
class UserRepository {
    private val MOCK = false
    fun modifyPassword(password: String, token: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).modifyPassword(password , "", "", token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }

    }

    fun verifySmsCode(phoneNumber: String, ac: Int, verifyCode: String): Observable<Boolean> {
        return API.appClient.create(UserApi::class.java).verifySmsCode(phoneNumber, ac, verifyCode)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map true
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }

    fun uploadBusinessLicenceCard(token: String, contactName: String, companyName: String, filePath: String): Observable<User> {
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), filePath)
        val body =  MultipartBody.Part.createFormData("fileName", File(filePath).name, requestBody)
        return API.appClient.create(UserApi::class.java)
                .uploadBusinessLicenceCard(token, contactName, companyName, body)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun uploadBusinessCard(token: String, content: String, filePath: String): Observable<User> {
        val requestBody = RequestBody.create(MediaType.parse("image/jpeg"), filePath)
        val body =  MultipartBody.Part.createFormData("fileName", File(filePath).name, requestBody)
        return API.appClient.create(UserApi::class.java)
                .uploadBusinessCard(token, content, body)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun uploadContactCard(token: String, cardFrontFilePath: String, cardBackEndFilePath: String): Observable<User> {
        val requestBody1 = RequestBody.create(MediaType.parse("image/jpeg"), cardFrontFilePath)
        val requestBody2 = RequestBody.create(MediaType.parse("image/jpeg"), cardBackEndFilePath)
        val body1 =  MultipartBody.Part.createFormData("fileName", File(cardFrontFilePath).name, requestBody1)
        val body2 =  MultipartBody.Part.createFormData("fileName2", File(cardBackEndFilePath).name, requestBody2)
        return API.appClient.create(UserApi::class.java)
                .uploadContactCard(token, body1, body2)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }
}