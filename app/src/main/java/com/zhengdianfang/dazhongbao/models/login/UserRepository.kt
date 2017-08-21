package com.zhengdianfang.dazhongbao.models.login

import com.fasterxml.jackson.core.type.TypeReference
import com.zhengdianfang.dazhongbao.models.api.API
import com.zhengdianfang.dazhongbao.models.api.CException
import com.zhengdianfang.dazhongbao.models.api.UserApi
import com.zhengdianfang.dazhongbao.models.mock.mockUser
import com.zhengdianfang.dazhongbao.models.mock.mockUserProducts
import com.zhengdianfang.dazhongbao.models.product.Product
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by dfgzheng on 08/08/2017.
 */
class UserRepository(private var MOCK: Boolean = false) {
    fun modifyPassword(password: String, token: String): Observable<String> {
        return API.appClient.create(UserApi::class.java).modifyPassword(password , "", "", token)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }

    }

    fun setPassword(password: String, token: String): Observable<User> {
        if (MOCK){
            return Observable.just(mockUser).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).modifyPassword(password , "", "", token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }

    }

    fun findPassword(password: String, verifyCode: String,  phoneNumber: String): Observable<String> {
        return API.appClient.create(UserApi::class.java).modifyPassword(password , phoneNumber, verifyCode, "")
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        return@map json.get("msg").asText()
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }

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

    fun modifyPhoneNumber(token: String, phoneNumber: String, verifyCode: String): Observable<User> {
        return API.appClient.create(UserApi::class.java).modifyPhoneNumber(token, phoneNumber, verifyCode)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue(data, User::class.java) }
    }

    fun fetchIndexCount(token: String): Observable<IntArray>{
        return API.appClient.create(UserApi::class.java).fetchIndexCount(token)
                .map {json ->
                    if(json.get("errCode").asInt() == 0){
                        val data = json.get("data")
                        return@map intArrayOf(data.get("DealCount").asInt(), data.get("ProductCount").asInt(), data.get("MessageCount").asInt())
                    }
                    throw CException(json.get("msg").asText(), json.get("errCode").asInt())
                }
    }

    fun fetchUserPushedProduct(token: String): Observable<MutableList<Product>> {
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).fetchUserPushedProduct(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){})}
    }

    fun fetchUserAttentionProducts(token: String): Observable<MutableList<Product>>{
        if (MOCK){
            return Observable.just(mockUserProducts).delay(2, TimeUnit.SECONDS)
        }
        return API.appClient.create(UserApi::class.java).fetchUserAttentionProducts(token)
                .map {response -> API.parseResponse(response) }
                .map {data -> API.objectMapper.readValue<MutableList<Product>>(data, object : TypeReference<MutableList<Product>>(){})}
    }
}